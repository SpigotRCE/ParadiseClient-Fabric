package io.github.spigotrce.paradiseclientfabric.mixin.inject.minecraft;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class to modify the behavior of the Minecraft Main class.
 * <p>
 * This class injects code into the main method of the Minecraft client
 * to set the "java.awt.headless" system property to "false". This is done
 * to ensure that AWT (Abstract Window Toolkit) is not in headless mode,
 * which can be necessary for certain graphical operations or debugging.
 * </p>
 *
 * @author SpigotRCE
 * @since 2.4
 */
@Mixin(Main.class)
public class MainMixin {

    /**
     * Injects code at the beginning of the main method of the Minecraft client.
     * <p>
     * This method sets the system property "java.awt.headless" to "false",
     * allowing AWT operations to be performed. This change affects the
     * graphical capabilities of the application.
     * </p>
     *
     * @param args Command-line arguments passed to the main method.
     * @param ci   Callback information.
     */
    @Inject(method = "main", at = @At(value = "HEAD"))
    private static void mainHead(String[] args, CallbackInfo ci) {
        System.setProperty("java.awt.headless", "false");
    }
}
