package com.crystals.plugin.services.message;

import com.crystals.plugin.utilities.Config;
import com.crystals.plugin.utilities.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

@RequiredArgsConstructor
public class MessageService {

    @NonNull
    private final Config config;

    public void sendMessage(CommandSender receiver, Message message) {
        sendMessage(receiver, message, Function.identity());
    }

    public void sendMessage(CommandSender receiver, Message message, Function<String, String> replacementFunction) {
        String path = message.getPath();
        String line = "&m--------------------------";
        if (config.isString(path))
            receiver.sendMessage(Utils.color(replacementFunction.apply(config.getString(path).replace("%line%", line))));
        else if (config.isList(path)) {
            // Join all strings of list with a newline character so one message packet can be used to convey the whole message
            String joinedMessage = String.join("\n", config.getStringList(path));
            receiver.sendMessage(Utils.color(replacementFunction.apply(joinedMessage.replace("%line%", line))));
        }
        else
            throw new IllegalArgumentException("Path \"" + path + "\" is not a string or list of strings in the config.yml");
    }

    public void sendMessage(Message message, CommandSender... receivers) {
        sendMessage(message, Function.identity(), receivers);
    }

    public void sendMessage(Message message, Function<String, String> replacementFunction, CommandSender... receivers) {
        for (CommandSender receiver : receivers)
            sendMessage(receiver, message, replacementFunction);
    }

    public <T extends CommandSender> void sendMessage(Message message, Iterable<T> receivers) {
        sendMessage(message, Function.identity(), receivers);
    }

    public <T extends CommandSender> void sendMessage(Message message, Function<String, String> replacementFunction, Iterable<T> receivers) {
        for (CommandSender receiver : receivers)
            sendMessage(receiver, message, replacementFunction);
    }

    public void broadcast(Message message) {
        sendMessage(message, Bukkit.getOnlinePlayers());
    }

    public void broadcast(Message message, Function<String, String> replacementFunction) {
        sendMessage(message, replacementFunction, Bukkit.getOnlinePlayers());
    }
}
