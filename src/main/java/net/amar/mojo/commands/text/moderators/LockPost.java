package net.amar.mojo.commands.text.moderators;

import org.json.JSONArray;

import net.amar.mojo.commands.TextInterface;
import net.amar.mojo.core.LoadData;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LockPost implements TextInterface{

    static JSONArray staff = LoadData.staff();

    @Override
    public String getName() {
        return "lock";
    }

    @Override
    public void executeMsg(MessageReceivedEvent event, String[] args) {
        if (event.getChannelType().isThread()){
            ThreadChannel ch = event.getChannel().asThreadChannel();
            if (ch.getParentChannel() instanceof ForumChannel){
                event.getGuild().retrieveMember(event.getAuthor()).queue(user->{
                boolean isUserStaff = user.getRoles().stream()
                .anyMatch((role) -> {
                for (int i = 0; i<staff.length(); i++){
                if (role.getId().equals(staff.getString(i))){
                 return true;
                        }
                     }
                 return false;
                 });

                    if (isUserStaff){
                        ch.sendMessage("Closing post by command from "+user.getAsMention()).queue();
                        ch.getManager().setLocked(true).queue();
                    } else {
                        ch.sendMessage("You are NOT a staff!").queue();
                    }
                });
            }
        }
    }
    
}
