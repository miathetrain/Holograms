package net.holograms;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import net.holograms.object.CraftHologram;
import net.holograms.object.CraftHologramLine;

import java.io.File;
import java.util.*;

public class HologramManager {

    private static HologramManager instance;
    private Map<String, CraftHologram> holograms = new HashMap<>();
    private Map<UUID, CraftHologram> currentlySelected = new HashMap<>();

    public HologramManager() {
        instance = this;
    }

    public static HologramManager getInstance() {
        return instance;
    }

    public Map<String, CraftHologram> getHolograms() {
        return holograms;
    }

    public CraftHologram createHologram(String name, Location loc, Boolean plugin) {
        if (plugin) {
            CraftHologram p = new CraftHologram(loc.add(0, 2), name);
            p.setCustom();
            holograms.put(name, p);
            return p;
        }
        CraftHologram particle = new CraftHologram(loc.add(0, 2), name);
        holograms.put(name, particle); // Location, CraftName
        return particle;
    }

    public void updateSelectedHologram(Player player) {
        CraftHologram closest = null;
        for (CraftHologram hologram : this.getHolograms().values()) {
            if (closest == null)
                closest = hologram;
            if (new Vector3(hologram.getX(), hologram.getY(), hologram.getZ()).distance(player) < new Vector3(closest.getX(), closest.getY(), closest.getZ()).distance(player)) {
                closest = hologram;
            }
        }
        currentlySelected.put(player.getUniqueId(), closest);
    }

    public CraftHologram getSelectedHologram(Player player) {
        return currentlySelected.get(player.getUniqueId());
    }

    public boolean deleteHologram(String arg) {
        if (!holograms.containsKey(arg))
            return false;
        holograms.get(arg).setDeleted();
        return true;
    }

    public String returnList() {
        String s = "";
        for (Map.Entry<String, CraftHologram> craft : holograms.entrySet()) {
            s += craft.getKey() + ", ";
        }
        return s;
    }


    public int addLine(String arg, CraftHologram hologram) {
        return hologram.addLine(arg);
    }

    public int removeLine(String arg, CraftHologram hologram) {
        hologram.deleteLine(arg);
        return Integer.parseInt(arg);
    }

    void save() {
        Config config = new Config(new File(Holograms.getInstance().getDataFolder(), "holograms.yml"), Config.YAML);

        /* Empty Config **/
        for(String s : config.getAll().keySet())
            config.remove(s);

        for (Map.Entry<String, CraftHologram> hologram : this.getHolograms().entrySet()) {
            if (hologram.getValue().getDeleted())
                continue;
            if(hologram.getValue().getCustom())
                continue;
            Map<String, Object> stuff = new HashMap<>();
            Map<String, Object> location = new HashMap<>();
            CraftHologram h = hologram.getValue();
            location.put("X", h.getX());
            location.put("Y", h.getY());
            location.put("Z", h.getZ());
            location.put("Level", h.getLevel().getName());
            stuff.put("Location", location);
            ArrayList<String> text = new ArrayList<>();
            for (CraftHologramLine line : h.getLines()) {
                if (!line.getDisabled())
                    text.add( line.getText());
            }

            stuff.put("Lines", text);

            config.set(hologram.getKey(), stuff);
        }
        config.save();
    }

    void load() {
        Config config = new Config(new File(Holograms.getInstance().getDataFolder(), "holograms.yml"), Config.YAML);
        for (Map.Entry<String, Object> object : config.getAll().entrySet()) { // THE BIG FILE // THE ORIGINAL HOLOGRAM NAME {HOLOGRAM OBJECT}
            holograms.put(object.getKey(), new CraftHologram(new Location(), object.getKey()));
            holograms.get(object.getKey()).updateLocation(Server.getInstance().getLevelByName(config.getString(object.getKey() + ".Location.Level")), config.getDouble(object.getKey() + ".Location.X"), config.getDouble(object.getKey() + ".Location.Y"), config.getDouble(object.getKey() + ".Location.Z"));
            List<String> lines = config.getStringList(object.getKey() + ".Lines");
            for (String stuff : lines) { // THESE ARE THE LINES
                holograms.get(object.getKey()).addLine(stuff);
            }
        }
    }
}
