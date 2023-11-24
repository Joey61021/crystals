package com.crystals.plugin.services.message;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Message {

    GENERIC_NO_CONSOLE("generic.messages.no-console"),
    GENERIC_CRYSTAL_FOUND("generic.messages.crystal.found"),
    GENERIC_CRYSTAL_ALREADY_FOUND("generic.messages.crystal.already-found"),
    GENERIC_CRYSTAL_FOUND_ALL("generic.messages.crystal.found-all"),

    CMD_CRYSTAL("commands.crystal.message"),

    CMD_CRYSTAL_RELOADED("commands.crystal.reloaded");

    @NonNull @Getter
    private final String path;
}
