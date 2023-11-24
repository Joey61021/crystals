package com.crystals.plugin;

import com.crystals.plugin.commands.CrystalCmd;
import com.crystals.plugin.listeners.EntityDamageListener;
import com.crystals.plugin.listeners.InteractAtEntityListener;
import com.crystals.plugin.listeners.PlayerDataListener;
import com.crystals.plugin.services.crystal.CrystalService;
import com.crystals.plugin.services.message.MessageService;
import com.crystals.plugin.utilities.Config;
import com.crystals.plugin.utilities.PAPIExpansion;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

//	Configs
	private Config config;
	private Config databaseConfig;
	private Config crystalsConfig;

//	Services
	private MessageService messageService;
	private CrystalService crystalService;

	@Override
	public void onEnable() {
		loadFiles();
		setupServices();
		registerEvents();
		registerCommands();

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) new PAPIExpansion(crystalService).register();

		crystalService.loadCrystals();
	}

	@Override
	public void onDisable() {
		crystalService.unloadCrystals();
	}

	void loadFiles() {
		config         = new Config(this, getDataFolder(), "config", "config.yml");
		databaseConfig = new Config(this, getDataFolder(), "database");
		crystalsConfig = new Config(this, getDataFolder(), "crystals", "crystals.yml");
	}

	void setupServices() {
		messageService = new MessageService(config);
		crystalService = new CrystalService(crystalsConfig, messageService);
	}

	void registerListener(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	void registerEvents() {
		registerListener(new EntityDamageListener(crystalService));
		registerListener(new InteractAtEntityListener(crystalService));
		registerListener(new PlayerDataListener(databaseConfig, crystalService));
	}

	@SuppressWarnings("all")
	void registerCommands() {
		getCommand("crystal").setExecutor(new CrystalCmd(messageService, crystalService));
	}
}
