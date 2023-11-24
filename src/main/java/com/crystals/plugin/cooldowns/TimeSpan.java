package com.crystals.plugin.cooldowns;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Setter
@AllArgsConstructor
public class TimeSpan {

    public long     amount;
    public TimeUnit timeUnit;
}
