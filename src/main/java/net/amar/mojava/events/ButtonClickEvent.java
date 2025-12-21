/*
Until I get to a point where I'd need a separate class for each button function,
this class will handle all the button clicks
 */

package net.amar.mojava.events;

import net.amar.mojava.Load;
import net.amar.mojava.Log;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonClickEvent extends ListenerAdapter{

    String pretesterRoleId = Load.getPretesterRoleId();
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();

        if (buttonId.equals("verify")) {
            Member member = event.getMember();
            Guild guild = event.getGuild();
            if (guild==null) {
                Log.error("couldn't find guild because its null!");
                return;
            }
            Role preTester = guild.getRoleById(pretesterRoleId);
            if (!(member==null || preTester==null)) {
                if (member.getRoles().contains(preTester)) {
                    event.reply("you already have the role").setEphemeral(true).queue();
                } else {
                    guild.addRoleToMember(member , preTester).queue(
                            success -> event.reply("added role successfully!").setEphemeral(true).queue(),
                            failure -> event.reply("failed to add role").setEphemeral(true).queue()
                    );
                }
            }
        }
    }
}
