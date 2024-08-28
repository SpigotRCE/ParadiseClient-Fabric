package io.github.spigotrce.paradiseclientfabric.mixin.inject.chat;

import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.github.spigotrce.paradiseclientfabric.event.ChatEvent;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {
    @Shadow
    ChatInputSuggestor chatInputSuggestor;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;sendMessage(Ljava/lang/String;Z)V", shift = At.Shift.BEFORE))
    private void keyPressedInvoke(CallbackInfoReturnable<Boolean> ci) {
        assert this.client != null;
        this.client.setScreen(null);
    }

//    @Inject(method = "keyPressed", at = @At(value = "HEAD"))
//    private void keyPressedHead(CallbackInfoReturnable<Boolean> ci)
//    {
//        System.out.println(this.chatInputSuggestor.toString());
//    }

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    public void sendMessageHead(String chatText, boolean addToHistory, CallbackInfo ci) {
        if (!ChatEvent.outgoingChatMessage(chatText)) {
            assert this.client != null;
            this.client.inGameHud.getChatHud().addToMessageHistory(chatText);

            ci.cancel();
        }
    }
}
