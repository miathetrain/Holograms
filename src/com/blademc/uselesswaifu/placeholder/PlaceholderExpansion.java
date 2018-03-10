package com.blademc.uselesswaifu.placeholder;

import com.blademc.uselesswaifu.FloatingPassage;

import java.util.List;


/**
 * Classes which extend this class are recognized as placeholder expansions
 * when they are located inside of the /plugins/placeholderapi/expansions/ folder.
 *
 * @author Ryan McCarthy
 */
public abstract class PlaceholderExpansion extends PlaceholderHook {

    /**
     * Check if a placeholder has already been registered with this identifier
     *
     * @return
     */
    public boolean isRegistered() {
        //Validate.notNull(getIdentifier() != null, "Placeholder identifier can not be null!");
        return PlaceholderAPI.getRegisteredPlaceholderPlugins().contains(getIdentifier());
    }

    /**
     * Attempt to register this PlaceholderExpansion with PlaceholderAPI
     *
     * @return true if this class and identifier have been successfully registered with PlaceholderAPI
     */
    public boolean register() {
        //Validate.notNull(getIdentifier() != null, "Placeholder identifier can not be null!");
        return PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);
    }

    /**
     * Quick getter for the {@link FloatingPassage} instance
     *
     * @return {@link FloatingPassage} instance
     */
    public FloatingPassage getPlaceholderAPI() {
        return FloatingPassage.getInstance();
    }

    /**
     * If any requirements are required to be checked before this hook can register, add them here
     *
     * @return true if this hook meets all the requirements to register
     */
    public abstract boolean canRegister();

    /**
     * Get the identifier that this placeholder expansion uses to be passed placeholder requests
     *
     * @return placeholder identifier that is associated with this class
     */
    public abstract String getIdentifier();

    /**
     * Get the plugin that this expansion hooks into.
     * This will ensure the expansion is added to a cache if canRegister() returns false
     * get.
     * The expansion will be removed from the cache
     * once a plugin loads with the name that is here and the expansion is registered
     *
     * @return placeholder identifier that is associated with this class
     */
    public abstract String getPlugin();

    /**
     * Get the author of this PlaceholderExpansion
     *
     * @return name of the author for this expansion
     */
    public abstract String getAuthor();

    /**
     * Get the version of this PlaceholderExpansion
     *
     * @return current version of this expansion
     */
    public abstract String getVersion();

    public String getString(String path, String def) {
        return getPlaceholderAPI().getConfig().getString("expansions." + getIdentifier() + "." + path, def);
    }

    public int getInt(String path, int def) {
        return getPlaceholderAPI().getConfig().getInt("expansions." + getIdentifier() + "." + path, def);
    }

    public long getLong(String path, long def) {
        return getPlaceholderAPI().getConfig().getLong("expansions." + getIdentifier() + "." + path, def);
    }

    public double getDouble(String path, double def) {
        return getPlaceholderAPI().getConfig().getDouble("expansions." + getIdentifier() + "." + path, def);
    }

    public List<String> getStringList(String path) {
        return getPlaceholderAPI().getConfig().getStringList("expansions." + getIdentifier() + "." + path);
    }

    public Object get(String path, Object def) {
        return getPlaceholderAPI().getConfig().get("expansions." + getIdentifier() + "." + path, def);
    }
}