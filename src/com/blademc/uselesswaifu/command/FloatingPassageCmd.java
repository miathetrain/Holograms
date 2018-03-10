package com.blademc.uselesswaifu.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.blademc.uselesswaifu.FloatingPassage;
import com.blademc.uselesswaifu.HologramManager;

;

public class FloatingPassageCmd extends Command {

    private static String SPACE_SEPARATOR = " ";
    private static FloatingPassageCmd instance;

    public FloatingPassageCmd(FloatingPassage instance) {
        super("floatingpassage");
        this.instance = this;
        setAliases(new String[]{"fp"});
        setUsage("/fp or /floatingpassage");
    }

    public static FloatingPassageCmd getInstance() {
        return instance;
    }

    @Override
    public boolean execute(CommandSender p, String alias, String[] args) {
        Player player = (Player) p;
        if (args.length < 1) {
            HelpCmd(player);
            return true;
        }

        if (args[0].equals("create")) {
            HologramManager.getInstance().createHologram(args[1], player.getLocation());
            HologramManager.getInstance().updateSelectedHologram(player);
            player.sendMessage(TextFormat.GRAY + "Hologram has been created titled " + TextFormat.YELLOW + args[1]);
            return true;
        }

        if (args[0].equals("delete")) {
            HologramManager.getInstance().deleteHologram(args[1]);
        }

        if (args[0].equals("list")) {
            player.sendMessage(HologramManager.getInstance().returnList());
        }

        if (args[0].equals("near")) {

        }

        if (args[0].equals("teleport")) {

        }

        /*
         *hd align <x|y|z|xz> <hologramToAlign> <referenceHologram>
         *Aligns the first hologram to the second in the given axis. For example, "y" aligns two holograms vertically, moving them to the same height, "xy" horizontally, moving them to the same x/z position.
         */
        if (args[0].equals("align")) { //

        }

        if (args[0].equals("movehere")) {

        }

        if (args[0].equals("addline")) {
            int index = HologramManager.getInstance().addLine(args[1], HologramManager.getInstance().getSelectedHologram(player));
            player.sendMessage(TextFormat.GRAY + "Line has been created! with index id of " + index);
            return true;
        }

        if (args[0].equals("removeline")) {
            int index = HologramManager.getInstance().removeLine(args[1], HologramManager.getInstance().getSelectedHologram(player));
            player.sendMessage(TextFormat.RED + "Line has been REMOVED! with index id of " + index);
        }

        if (args[0].equals("setline")) {

        }

        if (args[0].equals("insertline")) {

        }

        if (args[0].equals("info")) {

        }

        if (args[0].equals("readtext")) {

        }

        if (args[0].equals("copy")) {

        }

        if (args[0].equals("readimage")) {

        }

        if (args[0].equals("reload")) {

        }

        return true;
    }

    private void HelpCmd(Player player) {
        player.sendMessage(TextFormat.YELLOW + "FloatingPassage" + SPACE_SEPARATOR + TextFormat.BOLD.toString() + TextFormat.DARK_GRAY + "»");
        player.sendMessage(TextFormat.YELLOW + "/fp add" + SPACE_SEPARATOR + TextFormat.DARK_GRAY + "-" + SPACE_SEPARATOR + TextFormat.GRAY + "spawns a specific custom floating text.");
        player.sendMessage(TextFormat.YELLOW + "/fp del" + SPACE_SEPARATOR + TextFormat.DARK_GRAY + "-" + SPACE_SEPARATOR + TextFormat.GRAY + "despawns a specific custom floating text.");
        player.sendMessage(TextFormat.YELLOW + "/fp help" + SPACE_SEPARATOR + TextFormat.DARK_GRAY + "-" + SPACE_SEPARATOR + TextFormat.GRAY + "issues this help page.");
        player.sendMessage(TextFormat.BOLD + TextFormat.GRAY.toString() + "«" + SPACE_SEPARATOR + "»");
    }
}