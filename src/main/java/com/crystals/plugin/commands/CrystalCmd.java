package com.crystals.plugin.commands;

import com.crystals.plugin.services.crystal.CrystalService;
import com.crystals.plugin.services.message.Message;
import com.crystals.plugin.services.message.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class CrystalCmd implements CommandExecutor {

	@NonNull
	private final MessageService messageService;
	@NonNull
	private final CrystalService crystalService;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if (!(sender instanceof Player)) {
			messageService.sendMessage(sender, Message.GENERIC_NO_CONSOLE);
			return false;
		}
		Player player = (Player) sender;
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("reload") && player.hasPermission("crystal.reload")) {
				crystalService.unloadCrystals();
				crystalService.loadCrystals();
				messageService.sendMessage(player, Message.CMD_CRYSTAL_RELOADED);
				return false;
			}
		}
		messageService.sendMessage(player,
									Message.CMD_CRYSTAL,
									(s) -> s.replace("%found%", String.valueOf(crystalService.getFoundCrystalsList(player).size()))
											.replace("%total%", String.valueOf(crystalService.getCrystals().size() - 1)));
		return false;
	}
}
