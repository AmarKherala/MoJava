package net.amar.mojo.commands.text.moderators;

import org.json.JSONArray;

import net.amar.mojo.commands.TextInterface;
import net.amar.mojo.core.LoadData;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LockPost implements TextInterface{

  static String label;
    static JSONArray staff = LoadData.staff();
    @Override
    public String Catagory() {
      return "staff";
    }

    @Override
    public String getName() {
        return "lock";
    }

    @Override
    public String Description() {
      return "locks a support post";
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
           label = "";
          if (args.length > 0){
            label = "["+args[0]+"]";
          } else {
            label = "[RESOLVED]";
          }
                    if (isUserStaff || event.getAuthor().getId().equals(ch.getOwnerId())){
                        ch.getManager().setLocked(true).queue(success -> {
              ch.sendMessage("Closing post via request by "+event.getAuthor().getAsMention()).queue(); 
              ch.getManager().setName(label+" "+ch.getName()).queueAfter(1, java.util.concurrent.TimeUnit.SECONDS);
            });
                    }
                });
            }
        }
    }
    
}
