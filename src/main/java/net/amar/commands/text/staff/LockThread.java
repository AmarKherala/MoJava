package net.amar.commands.text.staff;

import net.amar.Load;
import net.amar.commands.CommandCategories;
import net.amar.commands.text.TextCommand;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;

public class LockThread implements TextCommand{

    static JSONArray arr = Load.getStaffRoles();

    @Override
    public CommandCategories getCategory(){
        return CommandCategories.STAFF;
    }

    @Override
    public String getName(){
        return "lock";
    }

    @Override
    public String getDescription(){
        return "lock a post after its purpose is done";
    }

    @Override
    public void executeMessage(MessageReceivedEvent event , String[] args){
        if(! event.getChannelType().isThread()) return;

        event.getGuild().retrieveMember(event.getAuthor()).queue(
                member -> {
                    boolean isMemberStaff = member.getRoles().stream()
                            .anyMatch(
                                    role -> {
                                        for(int i = 0 ; i < arr.length() ; i++) {
                                            if(role.getId().equals(arr.getString(i))) return true;
                                        }
                                        return false;
                                    }
                            );

                    ThreadChannel ch = event.getChannel().asThreadChannel();
                    long authorId = event.getAuthor().getIdLong();
                    long threadOpId = ch.getOwnerIdLong();

                    if (isMemberStaff && ch.isLocked()) {
                        event.getMessage().reply("thread is already locked").queue();
                        return;
                    }

                    if ((isMemberStaff || authorId == threadOpId) && args.length < 1){
                        event.getMessage().reply("please specify a label by doing `m!lock <label>`").queue();
                        return;
                    }
                    if (isMemberStaff || authorId == threadOpId) {

                        StringBuilder label = new StringBuilder();
                        for (String arg : args) {
                            label.append(arg).append(" ");
                        }
                        ch.getManager().setLocked(true).setName("["+label+"] "+ch.getName()).queue(
                                success -> event.getMessage().replyFormat("thread successfully locked with label [%s]",label).queue(),
                                failure -> event.getMessage().reply("failed to lock thread").queue()
                        );
                    }
                }
        );
    }
}
