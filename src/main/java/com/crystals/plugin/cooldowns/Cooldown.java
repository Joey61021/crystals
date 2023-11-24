package com.crystals.plugin.cooldowns;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class Cooldown {

    public long     timestamp;
    public TimeSpan timeSpan;
}
