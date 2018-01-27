package com.blademc.uselesswaifu.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.blademc.uselesswaifu.FloatingPassage;
import com.blademc.uselesswaifu.object.CraftParticle;;

public class FloatingPassageCmd extends Command {

    private Map<String, CraftParticle> holograms = new HashMap<>();
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
            test = new CraftParticle(player.getLocation().add(0, 2), args[1]); // Location, CraftName
            player.sendMessage(TextFormat.GRAY + "FloatingPassage has been created");
            return true;
        }

        if(args[0].equals("delete")){
            if(holograms.containsKey(args[1]))
                holograms.get(args[1]).setDeleted();
        }

        if(args[0].equals("list")){

        }

        if(args[0].equals("near")){

        }

        if(args[0].equals("teleport")){

        }

        /*
         *hd align <x|y|z|xz> <hologramToAlign> <referenceHologram>
         *Aligns the first hologram to the second in the given axis. For example, "y" aligns two holograms vertically, moving them to the same height, "xy" horizontally, moving them to the same x/z position.
         */
        if(args[0].equals("align")){ //

        }

        if(args[0].equals("movehere")){

        }

        if(args[0].equals("addline")){
           int index = test.addLine(args[1]);
           player.sendMessage(TextFormat.GRAY + "Line has been created! with index id of " + index);
            return true;
        }

        if(args[0].equals("removeline")){
            test.delLine(args[1]);
        }

        if(args[0].equals("setline")){

        }

        if(args[0].equals("insertline")){

        }

        if(args[0].equals("info")){

        }

        if(args[0].equals("readtext")){

        }

        if(args[0].equals("copy")){

        }

        if(args[0].equals("readimage")){

        }

        if(args[0].equals("reload")){

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