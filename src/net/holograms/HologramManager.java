package net.holograms;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import net.holograms.object.CraftParticle;
import net.holograms.object.CraftParticleLine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramManager {

    private static HologramManager instance;
    private Map<String, CraftParticle> holograms = new HashMap<>();
    private Map<UUID, CraftParticle> currentlySelected = new HashMap<>();

    public HologramManager() {
        instance = this;
    }

    public static HologramManager getInstance() {
        return instance;
    }

    public Map<String, CraftParticle> getHolograms() {
        return holograms;
    }

    public void createHologram(String name, Location loc) {
        holograms.put(name, new CraftParticle(loc.add(0, 2), name)); // Location, CraftName
    }

    public void updateSelectedHologram(Player player) {
        CraftParticle closest = null;
        for (CraftParticle hologram : this.getHolograms().values()) {
            if (closest == null)
                closest = hologram;
            if (new Vector3(hologram.getX(), hologram.getY(), hologram.getZ()).distance(player) < new Vector3(closest.getX(), closest.getY(), closest.getZ()).distance(player)) {
                closest = hologram;
            }
        }
        currentlySelected.put(player.getUniqueId(), closest);
    }

    public CraftParticle getSelectedHologram(Player player) {
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
        for (Map.Entry<String, CraftParticle> craft : holograms.entrySet()) {
            s += craft.getKey() + ", ";
        }
        return s;
    }


    public int addLine(String arg, CraftParticle hologram) {
        return hologram.addLine(arg);
    }

    public int removeLine(String arg, CraftParticle hologram) {
        hologram.deleteLine(arg);
        return Integer.parseInt(arg);
    }

    void save() {
        Config config = new Config(new File(FloatingPassage.getInstance().getDataFolder(), "holograms.yml"), Config.YAML);
        for (Map.Entry<String, CraftParticle> hologram : this.getHolograms().entrySet()) {
            if (hologram.getValue().getDeleted())
                continue;
            Map<String, Object> stuff = new HashMap<>();
            Map<String, Object> location = new HashMap<>();
            CraftParticle h = hologram.getValue();
            location.put("X", h.getX());
            location.put("Y", h.getY());
            location.put("Z", h.getZ());
            location.put("Level", h.getLevel().getName());
            stuff.put("Location", location);
            Map<String, Object> text = new HashMap<>();
            for (CraftParticleLine line : h.getLines()) {
                if (!line.getDisabled())
                    text.put("Text" + Integer.toString(line.getIndex()), line.getText());
            }

            stuff.put("Lines", text);

            config.set(hologram.getKey(), stuff);
        }
        config.save();
    }

    void load() {
        Config config = new Config(new File(FloatingPassage.getInstance().getDataFolder(), "holograms.yml"), Config.YAML);
        for (Map.Entry<String, Object> object : config.getAll().entrySet()) { // THE BIG FILE // THE ORIGINAL HOLOGRAM NAME {HOLOGRAM OBJECT}
            holograms.put(object.getKey(), new CraftParticle(new Location(), object.getKey()));
            holograms.get(object.getKey()).updateLocation(Server.getInstance().getLevelByName(config.getString(object.getKey() + ".Location.Level")), config.getDouble(object.getKey() + ".Location.X"), config.getDouble(object.getKey() + ".Location.Y"), config.getDouble(object.getKey() + ".Location.Z"));
            for (Map.Entry<String, Object> stuff : ((Map<String, Object>) ((Map<String, Object>) object.getValue()).get("Lines")).entrySet()) { // THESE ARE THE LINES
                holograms.get(object.getKey()).addLine((String) stuff.getValue());
            }
        }
    }
}
