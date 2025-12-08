package net.amar.commands.text.staff;

import net.amar.Load;
import net.amar.Main;
import net.amar.commands.CommandCategories;
import net.amar.commands.text.TextCommand;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class SetStatus implements TextCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getDescription() {
        return "change the custom status for the bot";
    }

    @Override
    public void executeMessage(MessageReceivedEvent event, String[] args) {
        if (Load.getOwnerId() != event.getAuthor().getIdLong()) {
            event.getMessage().reply("command can only be executed by the owner").queue();
            return;
        }
        String state = args[0];
        String statusText = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        switch (state) {
            case "p" -> {
                Main.getJDA().getPresence().setActivity(Activity.playing(statusText));
                event.getMessage().replyFormat("now playing %s",statusText).queue();
            }
            case "w" -> {
                Main.getJDA().getPresence().setActivity(Activity.watching(statusText));
                event.getMessage().replyFormat("now watching %s",statusText).queue();
            }
            default -> event.getMessage().reply("something went wrong").queue();
        }
    }
}
