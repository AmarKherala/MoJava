package net.amar.mojo.commands.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.amar.mojo.commands.CmdInterface;
import net.amar.mojo.core.AmarLogger;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
@SuppressWarnings("null")
public class ModPing implements CmdInterface {

    private final Map<String, Long> lastUsage = new HashMap<>();
    @Override
    public String getName() {
       return "modping";
    }

    @Override
    public String getDescription() {
       return "only use when necessary";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        long now = System.currentTimeMillis();

        if (lastUsage.containsKey(userId)){
            long last = lastUsage.get(userId);
            long diff = now - last;
            if (diff < 10*60*1000){
                event.reply("""
                    You can only use modping once each 10 minutes, this happens to prevent spam.
                      **IF YOUR MODPING WAS USELESS YOU WILL GET MUTED FOR AT LEAST 5 DAYS!**
                """).setEphemeral(true).queue();
                AmarLogger.warn("User! "+event.getUser().getName()+" tried to exexute modping before 10 minutes of thier last execution");
                return;
            }
        }
        lastUsage.put(userId, now);

        try{

        String json = new String(Files.readAllBytes(Paths.get("src/main/resources/staff.json")));

        JSONObject obj = new JSONObject(json);
        JSONArray arr = obj.getJSONArray("modping_roles");

        List<Role> modRoles = new ArrayList<>();

        for (int i=0; i < arr.length(); i++){
            Role r = event.getGuild().getRoleById(arr.getString(i));
            if (r!=null){
                modRoles.add(r);
            }
        }

        Set<Member> staffs = new HashSet<>();

        for (Role r: modRoles){
            staffs.addAll(event.getGuild().getMembersWithRoles(r));
        }

        List<Member> staffList = new ArrayList<>(staffs);
        Collections.shuffle(staffList);
        int four = Math.min(4, staffList.size());
        List<Member> theStaffs = staffList.subList(0, four);

        StringBuilder pings = new StringBuilder();
for (Member m : theStaffs) {
    pings.append(m.getAsMention()).append(" ");
}

 event.reply("**NOTE**: if your modping was useless you WILL get punished!\n" + pings.toString().trim()).queue();
 AmarLogger.info("User : "+event.getUser().getName()+" Tried to execute modping");

        } catch (IOException | JSONException e){
            AmarLogger.error("Something went wrong while executing modping", e);
        }
    }
    
}