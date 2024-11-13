package io.github.spigotrce.paradiseclientfabric.mixin.inject.bungee;

import com.google.common.base.Preconditions;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.mixin.accessor.BungeeCordAccessor;
import io.github.spigotrce.paradiseclientfabric.mod.BridgeMod;
import jline.console.ConsoleReader;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.compress.CompressFactory;
import net.md_5.bungee.log.BungeeLogger;
import net.md_5.bungee.log.LoggingForwardHandler;
import net.md_5.bungee.log.LoggingOutputStream;
import org.fusesource.jansi.AnsiConsole;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(BungeeCord.class)
public abstract class BungeeCordMixin implements BungeeCordAccessor {
    @Shadow(remap = false)
    @Final private Collection<String> pluginChannels;
    @Shadow(remap = false) public abstract PluginManager getPluginManager();

    @Shadow public abstract void reloadMessages();
    
    @Mutable
    @Final
    @Shadow private Logger logger;
    @Mutable
    @Final
    @Shadow private ConsoleReader consoleReader;
    @Mutable
    @Final
    @Shadow public PluginManager pluginManager;

    @Unique private ServerInfo targetServer;

//    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/md_5/bungee/BungeeCord;registerChannel(Ljava/lang/String;)V"), remap = false)
//    private void init(CallbackInfo ci) {
//
//    }

    @Inject(method = "start", at = @At("HEAD"), remap = false)
    public void startH(CallbackInfo ci) {
//        try {
//            construct();
//        } catch (IOException e) {
//            Constants.LOGGER.error("Unable to boot BungeeCord proxy - start", e);
//            BridgeMod.instance = null;
//        }
    }

    @Inject(method = "start", at = @At("TAIL"), remap = false)
    public void startT(CallbackInfo ci) {
        getPluginManager().getCommands().clear(); // We don't want any commands to be dropped by the proxy
        pluginChannels.clear(); // We don't want any channels to be dropped by the proxy
    }

    @Inject(method = "getServerInfo", at = @At("HEAD"), cancellable = true, remap = false)
    private void getServerInfo(String name, CallbackInfoReturnable<ServerInfo> ci) {
        ci.setReturnValue(targetServer);
    }

    @Unique
    private ServerInfo registerServer(String address, int port) {
        ProxyServer proxy = net.md_5.bungee.BungeeCord.getInstance();
        proxy.getServers().clear();
        InetSocketAddress serverAddress = new InetSocketAddress(address, port);
        ServerInfo serverInfo = proxy.constructServerInfo("bridge", serverAddress, "Bridge", false);
        proxy.getServers().put("bridge", serverInfo);

        return serverInfo;
    }

    @Override
    public void paradiseClient_Fabric$setTargetServer(String host, int port) {
        targetServer = registerServer(host, port);
    }

    @Unique
    public void construct() throws IOException {
    Preconditions.checkState((new File(".")).getAbsolutePath().indexOf(33) == -1, "Cannot use BungeeCord in directory with ! in path.");
        reloadMessages();
        System.setProperty("library.jansi.version", "BungeeCord");
        AnsiConsole.systemInstall();
        consoleReader = new ConsoleReader();
        consoleReader.setExpandEvents(false);
        logger = new BungeeLogger("BungeeCord", "proxy.log", consoleReader);
        Logger rootLogger = Logger.getLogger("");
        Handler[] var2 = rootLogger.getHandlers();

        for (Handler handler : var2) {
            rootLogger.removeHandler(handler);
        }

        rootLogger.addHandler(new LoggingForwardHandler(logger));
        System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
        pluginManager = new PluginManager(BungeeCord.getInstance());
        if (!Boolean.getBoolean("net.md_5.bungee.native.disable")) {
            if (EncryptionUtil.nativeFactory.load()) {
                logger.info("Using mbed TLS based native cipher.");
            } else {
                logger.info("Using standard Java JCE cipher.");
            }

            if (CompressFactory.zlib.load()) {
                logger.info("Using zlib based native compressor.");
            } else {
                logger.info("Using standard Java compressor.");
            }
        }
    }
}
