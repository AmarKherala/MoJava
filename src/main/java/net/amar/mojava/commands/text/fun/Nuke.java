package net.amar.mojava.commands.text.fun;

import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.text.TextCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Random;

public class Nuke implements TextCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.FUN;
    }

    @Override
    public String getName() {
        return "nuke";
    }

    @Override
    public String getDescription() {
        return "nuke a person you don't like";
    }

    @Override
    public void executeMessage(MessageReceivedEvent event, String[] args) {
        String wWhatToNuke = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        String[] respondsIfTargetIsntSelf = {
                "Roger, heading to nuke "+wWhatToNuke+" now.",
                "_"+wWhatToNuke+"_ too tiny, sending microscopic nuke sir.",
                "Sir, that taget is too cute I can't nuke _"+wWhatToNuke+"_.. :3",
                "You can't just nuke people around, that's cruel... poor _"+wWhatToNuke+"_..",
                "Sir, we sent our whole territory to _"+wWhatToNuke+"_ but they keep dodging them?? hows's that possible???",
        };
        Random ran = new Random();
        event.getMessage().reply(respondsIfTargetIsntSelf[ran.nextInt(respondsIfTargetIsntSelf.length)]).queue();
    }
}
