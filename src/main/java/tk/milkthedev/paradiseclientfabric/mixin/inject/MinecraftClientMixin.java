package tk.milkthedev.paradiseclientfabric.mixin.inject;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.milkthedev.paradiseclientfabric.Constants;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    @Inject(method = "getWindowTitle", at = @At(
            value = "INVOKE",
            target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            ordinal = 1),
            cancellable = true)
    private void getClientTitle(CallbackInfoReturnable<String> callback)
    {
        StringBuilder titleBuilder = new StringBuilder();
        titleBuilder.append("ParadiseClient-Fabric -v ");
        titleBuilder.append(Constants.VERSION);
        titleBuilder.append(" | ");
        titleBuilder.append(SharedConstants.getGameVersion().getName());
        callback.setReturnValue(titleBuilder.toString());
    }
}
