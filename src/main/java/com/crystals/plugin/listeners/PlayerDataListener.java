package com.crystals.plugin.listeners;

import com.crystals.plugin.utilities.Config;
import com.crystals.plugin.services.crystal.CrystalService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

    @NonNull
    private final Config         databaseConfig;
    @NonNull
    private final CrystalService crystalService;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (databaseConfig.get(uuid + ".crystals") == null) return;
        crystalService.getFoundCrystals().put(uuid, databaseConfig.getString(uuid + ".crystals"));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        databaseConfig.set(uuid + ".crystals", crystalService.getFoundCrystals().get(uuid));
        databaseConfig.save();
        crystalService.getFoundCrystals().remove(uuid);
    }
}
