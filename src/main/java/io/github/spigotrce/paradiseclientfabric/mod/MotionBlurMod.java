package io.github.spigotrce.paradiseclientfabric.mod;

import io.github.spigotrce.paradiseclientfabric.Constants;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public class MotionBlurMod {
    public boolean disabled;
    public int blurAmount;
    private ManagedShaderEffect motionBlur;


    public MotionBlurMod(boolean disabled, int blurAmount) {
        this.disabled = disabled;
        this.blurAmount = blurAmount;
        init();
    }

    public void init() {
        this.motionBlur = ShaderEffectManager.getInstance().manage(Identifier.of(Constants.MOD_ID, "shaders/post/motion_blur.json"));
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (getBlur() != 0 || !disabled) {
                if(blurAmount!=getBlur()){
                    motionBlur.setUniformValue("BlendFactor", getBlur());
                    blurAmount= (int) getBlur();
                }
                motionBlur.render(tickDelta);
            }
        });
    }

    public void toggle() {
        disabled =!disabled;
    }

    private float getBlur() {
        return Math.min(blurAmount, 99)/100F;
    }
}
