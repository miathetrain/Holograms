package com.blademc.uselesswaifu.format.expansion;

import cn.nukkit.Player;
import cn.nukkit.Server;
import com.blademc.uselesswaifu.placeholder.PlaceholderAPI;
import com.blademc.uselesswaifu.placeholder.PlaceholderExpansion;


public class PlayerExpansion extends PlaceholderExpansion {

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "player";
    }

    @Override
    public String getPlugin() {
        return null;
    }

    @Override
    public String getAuthor() {
        return "clip";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @SuppressWarnings("deprecation")
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {

        if (p == null) {
            return "";
        }

        if (identifier.startsWith("others_in_range_")) {

            String in = identifier.split("others_in_range_")[1];

            int i = 10;

            try {
                i = Integer.parseInt(in);
            } catch (NumberFormatException ex) {
                getPlaceholderAPI().getLogger().warning("Incorrect value for placeholder: %player_others_in_range_#%. Expected an integer, got: " + in);
            }

            return String.valueOf(getCountNearby(p, i));
        }

        if (identifier.startsWith("has_permission_")) {

            String perm = identifier.split("has_permission_")[1];

            if (perm == null || perm.isEmpty()) {
                return "";
            }

            return p.hasPermission(perm) ? PlaceholderAPI.TRUE : PlaceholderAPI.FALSE;
        }


        switch (identifier) {

            case "server":
            case "servername":
                return Server.getInstance().getName();
            case "name":
                return p.getName();
            case "displayname":
                return p.getDisplayName();
            case "uuid":
                return p.getUniqueId().toString();
            case "gamemode":
                return String.valueOf(p.getGamemode());
            case "world":
                return p.getLevel().getName();
            case "x":
                return String.valueOf(p.getLocation().getFloorX());
            case "y":
                return String.valueOf(p.getLocation().getFloorY());
            case "z":
                return String.valueOf(p.getLocation().getFloorZ());
            case "is_op":
                return p.isOp() ? PlaceholderAPI.TRUE : PlaceholderAPI.FALSE;
            case "ip":
                return p.getAddress();
            case "allow_flight":
                return p.getAllowFlight() ? PlaceholderAPI.TRUE : PlaceholderAPI.FALSE;
            case "ping":
                return Integer.toString(p.getPing());
            case "custom_name":
                return p.getDisplayName() != null ? p.getDisplayName() : p.getName();
            case "exp":
                return String.valueOf(p.getExperience());
            case "exp_to_level":
                return String.valueOf(p.getExperienceLevel());
            case "level":
                return String.valueOf(p.getLevel());
            case "food_level":
                return String.valueOf(p.getFoodData().getLevel());
            case "health":
                return String.valueOf(p.getHealth());
            case "item_in_hand":
                return p.getInventory().getItemInHand() != null ? p.getInventory().getItemInHand().getName() : "";
            case "last_damage":
                return String.valueOf(p.getLastDamageCause());
            case "max_health":
                return String.valueOf(p.getMaxHealth());
            case "max_air":
                return String.valueOf(p.getInAirTicks());
            case "time":
                return String.valueOf(p.getLevel().getTime());
        }
        return p.getName();

    }

    private int getCountNearby(Player p, int distance) {
        int i = 0;
        int d = distance * distance;
        for (Player other : p.getLevel().getPlayers().values()) {
            if (other.getName().equals(p.getName())) {
                continue;
            }
            if (p.getLocation().distanceSquared(other.getLocation()) <= d) {
                i = i++;
            }
        }
        return i;
    }


}