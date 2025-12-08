package net.amar.commands.text.staff;

import net.amar.Load;
import net.amar.Main;
import net.amar.commands.CommandCategories;
import net.amar.commands.text.TextCommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetOnlineStatus implements TextCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "onstatus";
    }

    @Override
    public String getDescription() {
        return "change the online status for the bot";
    }

    @Override
    public void executeMessage(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().getIdLong()== Load.getOwnerId()){
            String onlineStatus = args[0];
            switch (onlineStatus) {
                case "o","online"->{
                    Main.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
                    event.getMessage().reply("bot status is now online").queue();
                }
                case "i","idle"->{
                    Main.getJDA().getPresence().setStatus(OnlineStatus.IDLE);
                    event.getMessage().reply("bot status is now idle").queue();
                }
                case "dnd"->{
                    Main.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
                    event.getMessage().reply("bot status is now do not disturb").queue();
                }
                case "vanish"->{
                    Main.getJDA().getPresence().setStatus(OnlineStatus.INVISIBLE);
                    event.getMessage().reply("bot status is now invisible").queue();
                }
                default -> event.getMessage().reply("""
                        invalid status, available:
                        ``o``,``online``-> online
                        ``i``,``idle``-> idle
                        ``dnd``-> do not disturb
                        ``vanish``-> invisible""").queue();
            }
        } else {
            event.getMessage().reply("you cant use this command").queue();
        }
    }
}
