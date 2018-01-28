package com.blademc.uselesswaifu.placeholder;


import cn.nukkit.Player;

public abstract class PlaceholderHook {

    /**
     * called when a placeholder is requested from this PlaceholderHook
     * @param p Player object requesting the placeholder value for, null if not needed for a player
     * @param identifier placeholder identifier for the specific value
     * @return value for the requested player and identifier
     */

    public abstract String onPlaceholderRequest(Player p, String identifier);
}