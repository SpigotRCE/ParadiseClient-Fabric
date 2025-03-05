package io.github.spigotrce.paradiseclientfabric.chatroom.server.discord;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.DiscordWebhookSender;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Main;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.exception.UserAlreadyRegisteredException;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.exception.UserAlreadyVerifiedException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class DiscordBotImpl extends ListenerAdapter {
    public static void startDiscordBot() {
        Logging.info("Starting Discord Bot...");

        JDA jda = JDABuilder.createLight(Main.CONFIG.getDiscord().token(), EnumSet.noneOf(GatewayIntent.class)) // slash commands don't need any intents
                .addEventListeners(new DiscordBotImpl())
                .build();

        Logging.info("Discord Bot started");
        Logging.info("Logged in as: " + jda.getSelfUser().getId() + ":" + jda.getSelfUser().getName());

        CommandListUpdateAction commands = jda.updateCommands();

        Logging.info("Registering commands...");

        commands.addCommands(
                Commands.slash("paradise", "Creates a paradise account.")
                        .addOptions(new OptionData(STRING, "user_name", "Your username, remember this cannot be changed")
                                .setRequired(true))
                        .addOptions(new OptionData(STRING, "email", "If your discord account gets disabled, we will use this email to verify your identity.")
                                .setRequired(true))
                        .setContexts(InteractionContextType.GUILD)
        );

        commands.addCommands(
                Commands.slash("token", "Creates a paradise access token.")
                        .setContexts(InteractionContextType.GUILD)
        );

        commands.addCommands(
                Commands.slash("delete", "Deletes your account.")
                        .setContexts(InteractionContextType.GUILD)
        );

        commands.addCommands(
                Commands.slash("verify", "Verifies an account.")
                        .addOptions(new OptionData(STRING, "uuid", "UUID of the account to be verified.")
                                .setRequired(true))
                        .setContexts(InteractionContextType.GUILD)
        );

        commands.queue();
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return;
        if (event.getGuild().getIdLong() != Main.CONFIG.getDiscord().serverID())
            return;
        Member member = event.getMember();
        String commandName = event.getName();
        String options = event.getOptions().stream()
                .map(option -> option.getName() + ":" + option.getAsString())
                .collect(Collectors.joining(" "));

        String userTag = event.getUser().getAsTag();
        String userId = event.getUser().getId();

        Logging.info("%s:%s executed Discord command /%s %s".formatted(
                        userTag, userId, commandName, options.isEmpty() ? "" : options
                )
        );

        Logging.info("User:" + member.getUser().getId() + ":" + member.getUser().getName() + " executed discord command" + event.getFullCommandName());
        switch (event.getName()) {
            case "paradise":
                // No null-check since both params are required
                String userName = event.getOption("user_name").getAsString();
                String email = event.getOption("email").getAsString();

                try {
                    validateUsername(userName);
                } catch (IllegalArgumentException e) {
                    // ephemeral means only the command executor can see the message
                    event.reply(e.getMessage()).setEphemeral(true).queue();
                    return;
                }

                try {
                    validateEmail(email);
                } catch (IllegalArgumentException e) {
                    event.reply(e.getMessage()).setEphemeral(true).queue();
                    return;
                }

                UserModel userModel = new UserModel(member.getUser().getIdLong(), UUID.nameUUIDFromBytes(member.getId().getBytes(Charset.defaultCharset())), Date.valueOf(LocalDate.now()), userName, email, "", Main.CONFIG.getDiscord().autoVerify());

                try {
                    if (Main.DATABASE.getUser(userModel.discordID()) != null) {
                        event.reply("User already exists. Please use /paradise to create a new account.").setEphemeral(true).queue();
                        return;
                    }
                } catch (SQLException exception) {
                    Logging.error("SQL error for user with /paradise" + userModel.username(), exception);
                    event.reply("Failed to retrieve user information. Please try again later.").setEphemeral(true).queue();
                    return;
                }

                boolean verified;
                try {
                    verified = Main.registerNewUser(userModel);
                } catch (SQLException exception) {
                    Logging.error("Unable to register user: " + userModel.username(), exception);
                    event.reply("Failed to register user. Please try again later.").setEphemeral(true).queue();
                    return;
                } catch (UserAlreadyRegisteredException e) {
                    event.reply(e.getMessage()).setEphemeral(true).queue();
                    return;
                }

                sendWebhook(userModel.withVerified(verified), "User Registered");

                if (verified)
                    event.reply("User registered successfully! You can now use latest version of ParadiseClient-Fabric to connect to the chat server.").setEphemeral(true).queue();
                else {
                    event.reply("You are currently queued for verification. You'll receive a message when you get verified!").setEphemeral(true).queue();
                }

                break;
            case "delete":
                UserModel userModel0;
                try {
                    userModel0 = Main.DATABASE.getUser(member.getIdLong());
                    if (userModel0 == null) {
                        event.reply("User not found.").setEphemeral(true).queue();
                        return;
                    }
                    Main.DATABASE.deleteUser(member.getIdLong());
                    event.reply("User deleted successfully!").setEphemeral(true).queue();
                } catch (SQLException e) {
                    Logging.error("SQL error for user with /delete", e);
                    event.reply("Failed to retrieve user information. Please try again later.").setEphemeral(true).queue();
                    return;
                }
                sendWebhook(userModel0, "User Deleted");
                break;
            case "token":
                long id = member.getIdLong();
                UserModel user;
                try {
                    user = Main.DATABASE.getUser(id);
                } catch (SQLException e) {
                    Logging.error("SQL error for user with /token for user:" + id, e);
                    event.reply("Failed to retrieve user information. Please try again later.").setEphemeral(true).queue();
                    return;
                }

                if (!user.verified()) {
                    event.reply("Your account needs staff review before you can create an access token.").setEphemeral(true).queue();
                    return;
                }

                try {
                    user = Main.generateToken(user);
                } catch (SQLException e) {
                    Logging.error("Unable to generate token for user: " + user.username(), e);
                    event.reply("Failed to generate access token. Please try again later.").setEphemeral(true).queue();
                    return;
                }

                event.reply("Access token: `" + user.uuid() + "." + user.token() + "`").setEphemeral(true).queue();
                sendWebhook(user, "Access Token Created");
                break;
            case "verify":
                UUID uuid = UUID.fromString(event.getOption("uuid").getAsString());
                UserModel model;
                try {
                    model = Main.DATABASE.getUser(uuid);
                } catch (SQLException e) {
                    Logging.error("SQL error for user with /verify for uuid:" + uuid, e);
                    event.reply("Failed to retrieve user information. Please try again later.").setEphemeral(true).queue();
                    return;
                }
                try {
                    Main.verifyUser(uuid);
                } catch (UserAlreadyVerifiedException e) {
                    event.getChannel().sendMessage(e.getMessage()).queue();
                    return;
                } catch (SQLException e) {
                    Logging.error("Unable to verify user: " + uuid, e);
                    event.getChannel().sendMessage("Failed to verify user. Please try again later.").queue();
                    return;
                }
                event.getChannel().sendMessage("<@" + model.discordID() + "> verified successfully! You can now join the chat room! To create an access token, do `/token`").queue();
                sendWebhook(model, "User Verified");
                break;
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    private void validateUsername(String username) throws IllegalArgumentException {
        if (username.length() < 3 || username.length() > 32) {
            throw new IllegalArgumentException("Username must be between 3 and 32 characters long.");
        }
        if (!username.matches("^[a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("Username can only contain alphanumeric characters and underscores.");
        }
    }

    private void validateEmail(String email) throws IllegalArgumentException {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    private void sendWebhook(UserModel userModel, String message) {
        try {
            new DiscordWebhookSender(Main.CONFIG.getDiscord().webhookAccountLogging()).sendMessage(
                    """
                            {
                              "embeds": [
                                {
                                  "title": "MESSAGE",
                                  "color": 5814783,
                                  "fields": [
                                    {
                                      "name": "Discord ID",
                                      "value": "%d",
                                      "inline": true
                                    },
                                    {
                                      "name": "UUID",
                                      "value": "%s",
                                      "inline": true
                                    },
                                    {
                                      "name": "Date of Registration",
                                      "value": "%tF %tT",
                                      "inline": true
                                    },
                                    {
                                      "name": "Username",
                                      "value": "%s",
                                      "inline": true
                                    },
                                    {
                                      "name": "Email",
                                      "value": "%s",
                                      "inline": true
                                    },
                                    {
                                      "name": "Token",
                                      "value": "%s",
                                      "inline": true
                                    },
                                    {
                                      "name": "Verified",
                                      "value": "%b",
                                      "inline": true
                                    }
                                  ],
                                  "timestamp": "%tFT%tTZ"
                                }
                              ]
                            }
                            """.replaceAll("MESSAGE", message)
                            .formatted(
                                    userModel.discordID(),
                                    userModel.uuid(),
                                    userModel.dateOfRegistration(), userModel.dateOfRegistration(),
                                    userModel.username(),
                                    userModel.email(),
                                    userModel.token(),
                                    userModel.verified(),
                                    userModel.dateOfRegistration(), userModel.dateOfRegistration()
                            )
            );
        } catch (IOException exception) {
            Logging.error("Failed to send Discord webhook", exception);
        }
    }
}
