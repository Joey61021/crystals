package com.crystals.plugin.services.crystal;

import com.crystals.plugin.Main;
import com.crystals.plugin.services.message.Message;
import com.crystals.plugin.services.message.MessageService;
import com.crystals.plugin.utilities.Config;
import com.crystals.plugin.utilities.Utils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("all")
@RequiredArgsConstructor
public class CrystalService implements Listener {

	@NonNull
	private final Config         crystalConfig;
	@NonNull
	private final MessageService messageService;
	@NonNull
	private final Config         config;
	@Nullable
	private final Economy        economy;

	@Getter private String title;
	@Getter private World  world;
	@Getter private final ArrayList<Crystal> crystals         = new ArrayList<>();
	@Getter private final HashMap<UUID, String> foundCrystals = new HashMap<>();

	public void loadCrystals() {
		title = crystalConfig.getString("title");
		world = Bukkit.getWorld(crystalConfig.getString("world"));

		ConfigurationSection configSection = crystalConfig.getConfigurationSection("locations");
		Location location;
		for (String key : configSection.getKeys(false)) {
			ConfigurationSection section = configSection.getConfigurationSection(key);
			location = new Location(world,
									section.getDouble("x"),
									section.getDouble("y"),
									section.getDouble("z"),
									(float) section.getDouble("yaw"), 0);
			crystals.add(new Crystal(Integer.parseInt(key), createCrystal(location, Integer.parseInt(key))));
		}
	}

	public void unloadCrystals() {
		crystals.forEach(crystal -> crystal.getCrystal().remove());
		crystals.clear();
	}

	public Entity createCrystal(Location location, int identifier) {
		ArmorStand crystal = (ArmorStand) world.spawnEntity(location.add(0, -1.30, 0), EntityType.ARMOR_STAND);
		crystal.setCustomName(Utils.color(title));

		crystal.getEquipment().setHelmet(new ItemStack(Material.QUARTZ_BLOCK));
		crystal.setVisible(false);
		crystal.setGravity(false);
		crystal.setCustomNameVisible(true);
		crystal.setMetadata("crystal", new FixedMetadataValue(JavaPlugin.getPlugin(Main.class), identifier));
		return crystal;
	}

	public Crystal getCrystal(Entity entity) {
		return entity.hasMetadata("crystal") ? crystals.get(entity.getMetadata("crystal").get(0).asInt()) : null;
	}

	public void addCrystal(Player player, int identifier) {
		StringBuilder updated = new StringBuilder(foundCrystals.get(player.getUniqueId()) == null ? "" : foundCrystals.get(player.getUniqueId()));
		if (updated.length() > 0) updated.append(",");
		foundCrystals.put(player.getUniqueId(), updated.append(identifier).toString());
	}

	public List<Integer> getFoundCrystalsList(Player player) {
		if (foundCrystals.get(player.getUniqueId()) == null) return Collections.emptyList();
		List<Integer> list = new ArrayList<>();
		Arrays.stream(foundCrystals.get(player.getUniqueId()).split(",")).forEach(foundCrystals -> list.add(Integer.parseInt(foundCrystals)));
		return list;
	}

	public void foundCrystal(Player player, Crystal crystal) {
		try {
			int identifier = crystal.getIdentifier();
			List<Integer> foundCrystalsList = getFoundCrystalsList(player);
			if (foundCrystalsList.contains(identifier)) {
				messageService.sendMessage(player, Message.GENERIC_CRYSTAL_ALREADY_FOUND);
				return;
			}
			addCrystal(player, identifier);
			boolean all_found = foundCrystalsList.size()+1 == crystals.size()-1;
			double amount = config.getDouble(all_found ? "generic.rewards.found-all" : "generic.rewards.found");
			if (economy != null) economy.depositPlayer(player, amount);
			messageService.sendMessage(player,
										all_found ? Message.GENERIC_CRYSTAL_FOUND_ALL : Message.GENERIC_CRYSTAL_FOUND,
										(s) -> s.replace("%amount%", String.valueOf(amount))
												.replace("%found%", String.valueOf(foundCrystalsList.size()+1))
												.replace("%total%", String.valueOf(crystals.size()-1)));
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		} catch (NumberFormatException ignored) {}
	}
}
