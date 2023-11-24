package com.crystals.plugin.utilities;

import com.crystals.plugin.services.crystal.CrystalService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PAPIExpansion extends PlaceholderExpansion {

    @NonNull
    private final CrystalService crystalService;

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor(){
        return "Texxyy";
    }

    @Override
    public @NotNull String getIdentifier(){
        return "crystals";
    }

    @Override
    public @NotNull String getVersion(){
        return "1.0.0-SNAPSHOT";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return null;
        switch (identifier) {
            case "found": return String.valueOf(crystalService.getFoundCrystalsList(player).size());
            case "total": return String.valueOf(crystalService.getCrystals().size());
        }
        return null;
    }
}
