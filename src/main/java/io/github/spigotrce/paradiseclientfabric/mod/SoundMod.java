package io.github.spigotrce.paradiseclientfabric.mod;

import io.github.spigotrce.paradiseclientfabric.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class SoundMod {
    public final ArrayList<SoundEvent> soundEvents;
    private final MinecraftClient minecraftClient;

    public SoundMod(MinecraftClient minecraftClient) {
        Constants.LOGGER.info("Registering sounds");
        this.minecraftClient = minecraftClient;
        this.soundEvents = new ArrayList<>();
        this.soundEvents.add(registerSoundEvent("matador"));
        this.soundEvents.add(registerSoundEvent("onmyway"));
        this.soundEvents.add(registerSoundEvent("2steppingwithmyglock"));
    }

    private SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Constants.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}