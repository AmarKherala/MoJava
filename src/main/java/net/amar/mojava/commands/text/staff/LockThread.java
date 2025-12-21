package net.amar.mojava.commands.text.staff;

import net.amar.mojava.Load;
import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.text.TextCommand;
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

                    if ((isMemberStaff || authorId == threadOpId) && args.length > 0 && args[0].equals("-f")){
                        ch.getManager().setName("[RESOLVED]").setLocked(true).queue();
                        event.getMessage().reply("successfully closed thread by force with label [RESOLVED]").queue();
                        return;
                    }
                    if (isMemberStaff && ch.isLocked()) {
                        event.getMessage().reply("thread is already locked").queue();
                        return;
                    }

                    if ((isMemberStaff || authorId == threadOpId) && args.length < 1){
                        event.getMessage().reply("please specify a label by doing `m!lock <label>`").queue();
                        return;
                    }

                    if (isMemberStaff || authorId == threadOpId) {

                        String label = String.join(" ", args);
                        String newName = "["+ label +"] " + ch.getName().replaceAll("\\[.*?\\]\\s*", "");

                        if (newName.length() > 100 ) {
                            event.getMessage().reply("post name may not be longer than 100 characters").queue();
                            return;
                        }

                        ch.getManager().setLocked(true).setName(newName).queue(
                                success -> event.getMessage().replyFormat("thread successfully locked with label [%s]",label).queue(),
                                failure -> event.getMessage().reply("failed to lock thread").queue()
                        );
                    }
                }
        );
    }
}
