package io.github.spigotrce.paradiseclientfabric.chatroom.server.discord;

import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.exception.UserAlreadyRegisteredException;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Main;
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

import java.sql.SQLException;
import java.util.EnumSet;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

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
                Commands.slash("paradise", "Ban a user from this server. Requires permission to ban users.")
                        .addOptions(new OptionData(STRING, "user_name", "Your username, remember this cannot be changed")
                                .setRequired(true))
                        .addOptions(new OptionData(STRING, "email", "If your discord account gets disabled, we will use this email to verify your identity.")
                                .setRequired(true))
                        .setContexts(InteractionContextType.GUILD)
        );

        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return;
        if (event.getGuild().getIdLong() != Main.CONFIG.getDiscord().serverID())
            return;
        switch (event.getName()) {
            case "paradise":
                Member member = event.getMember();

                // No null-check since both params are required
                String userName = event.getOption("user_name").getAsString();
                String email = event.getOption("email").getAsString();

                Logging.info("User:" + member.getUser().getName() + ":" + member.getUser().getName() + " executed discord command /paradise " + userName + " " + email);

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
                    // ephemeral means only the command executor can see the message
                    event.reply(e.getMessage()).setEphemeral(true).queue();
                    return;
                }

                UserModel userModel = new UserModel(userName, email, "");

                boolean verified;
                try {
                    verified = !Main.registerNewUser(userModel);
                } catch (SQLException exception) {
                    Logging.error("Unable to register user: " + userModel.username(), exception);
                    event.reply("Failed to register user. Please try again later.").setEphemeral(true).queue();
                    return;
                } catch (UserAlreadyRegisteredException e) {
                    event.reply(e.getMessage()).setEphemeral(true).queue();
                    return;
                }

                if (verified)
                    // TODO: Send message to create new token
                    event.reply("User registered successfully! You can now use latest version of ParadiseClient-Fabric to connect to the chat server.").setEphemeral(true).queue();
                else
                    event.reply("You are currently queued for verification. You'll receive a message when you get verified!").setEphemeral(true).queue();

                event.reply("Thanks for applying to the chatroom!").setEphemeral(true).queue();
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
}
