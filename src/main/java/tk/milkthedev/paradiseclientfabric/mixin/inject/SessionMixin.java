package tk.milkthedev.paradiseclientfabric.mixin.inject;

import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.*;
import tk.milkthedev.paradiseclientfabric.mixin.accessor.SessionAccessor;

@Mixin(Session.class)
public class SessionMixin implements SessionAccessor
{
    @Final
    @Shadow
    @Mutable
    private String username;

    @Override
    public void paradiseClient_Fabric$setUsername(String username) {this.username = username;}
}