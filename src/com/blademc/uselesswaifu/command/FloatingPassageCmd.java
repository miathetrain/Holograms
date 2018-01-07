package com.blademc.uselesswaifu.command;

import java.util.Arrays;


import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import com.blademc.uselesswaifu.FloatingPassage;
import com.blademc.uselesswaifu.object.CraftParticle;;

public class FloatingPassageCmd extends Command {

    private static String SPACE_SEPARATOR = " ";
    private FloatingPassage instance;
    private CraftParticle test;

    public FloatingPassageCmd(FloatingPassage instance) {
        super("floatingpassage");
        this.instance = instance;
        setAliases(new String[]{"fp"});
        setUsage("/fp or /floatingpassage");
    }

    @Override
    public boolean execute(CommandSender p, String alias, String[] args) {
        Player player = (Player) p;
        if(args.length < 1){
            HelpCmd(player);
            return true;
        }

        if(args[0].equals("create")){
            test = new CraftParticle(player.getLocation());
            player.sendMessage(TextFormat.GRAY + "FloatingPassage has been created");
            return true;
        }

        if(args[0].equals("addline")){
           int index = test.addLine(args[1]);
           player.sendMessage(TextFormat.GRAY + "Line has been created! with index id of " + index);
            return true;
        }

        if(args[0].equals("delline")){
            test.delLine(args[1]);
        }

        return true;
    }

    public void HelpCmd(Player player){
        player.sendMessage(TextFormat.YELLOW  + "FloatingPassage" + SPACE_SEPARATOR + TextFormat.BOLD.toString() + TextFormat.DARK_GRAY + "»");
        player.sendMessage(TextFormat.YELLOW + "/fp add" + SPACE_SEPARATOR + TextFormat.DARK_GRAY + "-" + SPACE_SEPARATOR + TextFormat.GRAY + "spawns a specific custom floating text.");
        player.sendMessage(TextFormat.YELLOW + "/fp del" + SPACE_SEPARATOR + TextFormat.DARK_GRAY + "-" + SPACE_SEPARATOR + TextFormat.GRAY + "despawns a specific custom floating text.");
        player.sendMessage(TextFormat.YELLOW + "/fp help" + SPACE_SEPARATOR + TextFormat.DARK_GRAY + "-" + SPACE_SEPARATOR + TextFormat.GRAY + "issues this help page.");
        player.sendMessage(TextFormat.BOLD + TextFormat.GRAY.toString() + "«" + SPACE_SEPARATOR + "»");
    }
}