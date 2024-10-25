package io.github.spigotrce.paradiseclientfabric.mixin.inject.auth;

import io.github.spigotrce.paradiseclientfabric.mixin.accessor.SessionAccessor;
import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mixin class for modifying the behavior of the Session class.
 * <p>
 * This class implements the SessionAccessor interface to provide access
 * to private fields of the Session class and modify the username.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.0
 */
@SuppressWarnings("unused")
@Mixin(Session.class)
public class SessionMixin implements SessionAccessor {

    @Final
    @Shadow
    @Mutable
    private String username;

    /**
     * Sets the username in the Session class.
     * <p>
     * This method allows modification of the private username field in the
     * Session class through the SessionAccessor interface.
     * </p>
     *
     * @param username The new username to set.
     */
    @Override
    public void paradiseClient_Fabric$setUsername(String username) {
        this.username = username;
    }
}
