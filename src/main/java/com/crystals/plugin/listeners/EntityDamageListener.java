package com.crystals.plugin.listeners;

import com.crystals.plugin.services.crystal.CrystalService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@RequiredArgsConstructor
public class EntityDamageListener implements Listener {

	@NonNull
	private final CrystalService crystalService;

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (crystalService.getCrystal(event.getEntity()) == null) return;
		event.setCancelled(true);
	}
}
