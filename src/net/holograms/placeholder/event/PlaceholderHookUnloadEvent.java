package net.holograms.placeholder.event;


import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import net.holograms.placeholder.PlaceholderHook;

public class PlaceholderHookUnloadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private String plugin;
    private PlaceholderHook hook;

    public PlaceholderHookUnloadEvent(String plugin, PlaceholderHook placeholderHook) {
        this.plugin = plugin;
        this.hook = placeholderHook;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public String getHookName() {
        return plugin;
    }

    public PlaceholderHook getHook() {
        return hook;
    }
}