package com.crystals.plugin.listeners;

import com.crystals.plugin.services.crystal.Crystal;
import com.crystals.plugin.services.crystal.CrystalService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

@RequiredArgsConstructor
public class InteractAtEntityListener implements Listener {

	@NonNull
	private final CrystalService crystalService;
	
	@EventHandler
	public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
		try {
			if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
				Crystal crystal;
				if ((crystal = crystalService.getCrystal(event.getRightClicked())) == null) return;
				event.setCancelled(true);
				crystalService.foundCrystal(event.getPlayer(), crystal);
			}
		} catch (Exception ignored) {}
	}
}
