package com.crystals.plugin.services.crystal;

import lombok.Getter;
import org.bukkit.entity.Entity;

public class Crystal {

    @Getter
    private final int    identifier;
    @Getter
    private final Entity crystal;

    public Crystal(int identifier, Entity crystal) {
        this.identifier = identifier;
        this.crystal    = crystal;
    }
}
