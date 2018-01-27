package com.blademc.uselesswaifu.placeholder;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import com.blademc.uselesswaifu.placeholder.event.PlaceholderHookUnloadEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class PlaceholderAPI {

    private final static Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([^%]+)[%]");

    private final static Pattern BRACKET_PLACEHOLDER_PATTERN = Pattern.compile("[{]([^{}]+)[}]");

    public final static String TRUE = "true";
    public final static String FALSE = "false";

    private static Map<String, PlaceholderHook> placeholders = new HashMap<String, PlaceholderHook>();

    /**
     * unregister all placeholder hooks which were registered by PlaceholderAPI
     *
     */
    protected static void resetInternalPlaceholderHooks() {

        if (placeholders == null || placeholders.isEmpty()) {
            return;
        }

        for (Entry<String, PlaceholderHook> pl : getPlaceholders().entrySet()) {

            if (pl.getValue() instanceof PlaceholderExpansion) {
                Server.getInstance().getPluginManager().callEvent(new PlaceholderHookUnloadEvent(pl.getKey(), pl.getValue()));
                unregisterPlaceholderHook(pl.getKey());
            }
        }
    }

    /**
     * unregister ALL placeholder hooks that are currently registered
     */
    protected static void unregisterAll() {
        resetInternalPlaceholderHooks();
        placeholders = null;
    }

    /**
     * check if a specific placeholder identifier has already been registered
     * @param plugin
     * @return
     */
    public static boolean isRegistered(String plugin) {
        if (placeholders == null || placeholders.isEmpty()) {
            return false;
        }
        for (String pl : getRegisteredPlaceholderPlugins()) {
            if (pl.equals(plugin.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * registers a placeholder hook specific to the plugin specified. Any time a placeholder that matches the plugin name,
     * the method inside of the PlaceholderHook will be called to retrieve a value
     * placeholders always follow a specific format - %<plugin>_<identifier>%
     * The identifier is passed to the method which will provide the value. It is up to the registering plugin to determine what
     * a valid identifier is. If an identifier is unknown, you may return null which specifies the placeholder is invalid.
     * @param plugin Plugin registering the placeholder hook
     * @param placeholderHook PlaceholderHook class that contains the override method which is called when a value is needed for the specific plugins placeholder
     * @return true if the hook was successfully registered, false if there was already a hook registered for the specified plugin
     */
    public static boolean registerPlaceholderHook(Plugin plugin, PlaceholderHook placeholderHook) {

        if (plugin == null) {
            return false;
        }

        return registerPlaceholderHook(plugin.getName(), placeholderHook);
    }

    /**
     * registers a placeholder hook specific to the plugin name specified. Any time a placeholder that matches the plugin name,
     * the method inside of the PlaceholderHook will be called to retrieve a value
     * placeholders always follow a specific format - %<plugin>_<identifier>%
     * The identifier is passed to the method which will provide the value. It is up to the registering plugin to determine what
     * a valid identifier is. If an identifier is unknown, you may return null which specifies the placeholder is invalid.
     * @param plugin Plugin name registering the placeholder hook
     * @param placeholderHook PlaceholderHook class that contains the override method which is called when a value is needed for the specific plugins placeholder
     * @return true if the hook was successfully registered, false if there was already a hook registered for the specified plugin
     */
    public static boolean registerPlaceholderHook(String plugin, PlaceholderHook placeholderHook) {
        if (placeholders == null ) {
            placeholders = new HashMap<String, PlaceholderHook>();
        }

        if (plugin == null
                || placeholderHook == null
                || placeholders.containsKey(plugin)) {
            return false;
        }

        placeholders.put(plugin.toLowerCase(), placeholderHook);

        return true;
    }

    @Deprecated
    public static boolean registerPlaceholderHook(Plugin plugin, PlaceholderHook placeholderHook, boolean isInternalHook) {

        if (plugin == null) {
            return false;
        }

        return registerPlaceholderHook(plugin.getName(), placeholderHook);
    }

    @Deprecated
    public static boolean registerPlaceholderHook(String plugin, PlaceholderHook placeholderHook, boolean isInternalHook) {
        return registerPlaceholderHook(plugin, placeholderHook);
    }

    /**
     * unregister a placeholder hook for a specific plugin
     * @param plugin Plugin to unregister
     * @return true if the placeholder hook was successfully unregistered, false if there was no placeholder hook registered for the plugin specified
     */
    public static boolean unregisterPlaceholderHook(Plugin plugin) {

        if (plugin == null) {
            return false;
        }

        return unregisterPlaceholderHook(plugin.getName());
    }

    /**
     * unregister a placeholder hook for a specific plugin
     * @param plugin Plugin name to unregister
     * @return true if the placeholder hook was successfully unregistered, false if there was no placeholder hook registered for the plugin specified
     */
    public static boolean unregisterPlaceholderHook(String plugin) {

        if (plugin == null) {
            return false;
        }

        if (placeholders == null || placeholders.isEmpty()) {
            return false;
        }

        return placeholders.remove(plugin.toLowerCase()) != null;
    }

    /**
     * obtain the names of every placeholder "plugin" identifier that has been registered
     * @return Set of placeholder "plugin" identifiers currently registered
     */
    public static Set<String> getRegisteredPlaceholderPlugins() {

        if (placeholders == null || placeholders.isEmpty()) {
            return new HashSet<String>();
        }

        return new HashSet<String>(placeholders.keySet());
    }

    /**
     * obtain the names of every external placeholder "plugin" identifier that was not registered by PlaceholderAPI
     * @return Set of placeholder identifier names that PlaceholderAPI did not register placeholders for
     */
    public static Set<String> getExternalPlaceholderPlugins() {

        Set<String> external = new HashSet<String>();

        if (placeholders == null || placeholders.isEmpty()) {
            return external;
        }

        for (Entry<String, PlaceholderHook> pl : getPlaceholders().entrySet()) {

            if (!(pl.getValue() instanceof PlaceholderExpansion)) {
                external.add(pl.getKey());
            }
        }
        return external;
    }

    /**
     * obtain the map of registered placeholder hook plugins and the corresponding PlaceholderHooks registered
     * @return copy of the internal placeholder map.
     */
    public static Map<String, PlaceholderHook> getPlaceholders() {
        return new HashMap<String, PlaceholderHook>(placeholders);
    }

    /**
     * check if a String contains any valid PlaceholderAPI placeholders
     * @param text String to check
     * @return true if String passed contains any valid PlaceholderAPI placeholder identifiers, false otherwise
     */
    public static boolean containsPlaceholders(String text) {

        if (text == null || placeholders == null || placeholders.isEmpty()) {
            return false;
        }

        return PLACEHOLDER_PATTERN.matcher(text).find();

    }

    /**
     * check if a String contains any valid PlaceholderAPI bracket placeholders
     * @param text String to check
     * @return true if String passed contains any valid PlaceholderAPI placeholder identifiers, false otherwise
     */
    public static boolean containsBracketPlaceholders(String text) {

        if (text == null || placeholders == null || placeholders.isEmpty()) {
            return false;
        }

        return BRACKET_PLACEHOLDER_PATTERN.matcher(text).find();
    }

    /**
     * set placeholders in the list<String> text provided
     * placeholders are matched with the pattern {<placeholder>} when set with this method
     * @param p Player to set the placeholders for
     * @param text text to set the placeholder values in
     * @return original list with all valid placeholders set to the correct values if the list contains any valid placeholders
     */
    public static List<String> setBracketPlaceholders(Player p, List<String> text) {
        if (text == null) {
            return text;
        }
        List<String> temp = new ArrayList<String>();
        for (String line : text) {
            temp.add(setBracketPlaceholders(p, line));
        }
        return temp;
    }

    /**
     * set placeholders in the text specified
     * placeholders are matched with the pattern {<placeholder>} when set with this method
     * @param player Player to set the placeholders for
     * @param text text to set the placeholder values to
     * @return original text with all valid placeholders set to the correct values if the String contains valid placeholders
     */
    public static String setBracketPlaceholders(Player player, String text) {

        if (text == null || placeholders == null || placeholders.isEmpty()) {
            return text;
        }

        Set<Entry<String, PlaceholderHook>> entries = getPlaceholders().entrySet();

        Matcher placeholderMatcher = BRACKET_PLACEHOLDER_PATTERN.matcher(text);

        while (placeholderMatcher.find()) {

            String format = placeholderMatcher.group(1);

            int index = format.indexOf("_");

            if (index <= 0 || index >= format.length()) {
                continue;
            }

            String pl = format.substring(0, index);

            String identifier = format.substring(index+1);

            for (Entry<String, PlaceholderHook> e : entries) {

                if (pl.equalsIgnoreCase(e.getKey())) {

                    String value = e.getValue().onPlaceholderRequest(player, identifier);

                    if (value != null) {
                        text = text.replaceAll("\\{"+format+"\\}", Matcher.quoteReplacement(value));
                    }
                    break;
                }
            }
        }

        return text;
    }

    /**
     * set placeholders in the list<String> text provided
     * placeholders are matched with the pattern %<placeholder>% when set with this method
     * @param p Player to set the placeholders for
     * @param text text to set the placeholder values in
     * @return original list with all valid placeholders set to the correct values if the list contains any valid placeholders
     */
    public static List<String> setPlaceholders(Player p, List<String> text) {
        if (text == null) {
            return text;
        }
        List<String> temp = new ArrayList<String>();
        for (String line : text) {
            temp.add(setPlaceholders(p, line));
        }
        return temp;
    }

    /**
     * set placeholders in the text specified
     * placeholders are matched with the pattern %<placeholder>% when set with this method
     * @param player Player to set the placeholders for
     * @param text text to set the placeholder values to
     * @return original text with all valid placeholders set to the correct values if the String contains valid placeholders
     */
    public static String setPlaceholders(Player player, String text) {

        if (text == null || placeholders == null || placeholders.isEmpty()) {
            return text;
        }

        Matcher placeholderMatcher = PLACEHOLDER_PATTERN.matcher(text);

        Set<Entry<String, PlaceholderHook>> entries = getPlaceholders().entrySet();

        while (placeholderMatcher.find()) {

            String format = placeholderMatcher.group(1);

            int index = format.indexOf("_");

            if (index <= 0 || index >= format.length()) {
                continue;
            }

            String pl = format.substring(0, index);

            String identifier = format.substring(index+1);

            for (Entry<String, PlaceholderHook> e : entries) {

                if (pl.equalsIgnoreCase(e.getKey())) {

                    String value = e.getValue().onPlaceholderRequest(player, identifier);

                    if (value != null) {
                        text = text.replace("%"+format+"%", Matcher.quoteReplacement(value));
                    }
                }
            }
        }
        return text;
    }

    public static Pattern getPlaceholderPattern() {
        return PLACEHOLDER_PATTERN;
    }

    public static Pattern getBracketPlaceholderPattern() {
        return BRACKET_PLACEHOLDER_PATTERN;
    }
}