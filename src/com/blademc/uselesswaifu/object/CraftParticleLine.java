package com.blademc.uselesswaifu.object;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.utils.TextFormat;
import com.blademc.uselesswaifu.placeholder.PlaceholderAPI;
import com.blademc.uselesswaifu.placeholder.PlaceholderHook;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import static cn.nukkit.entity.Entity.DATA_NAMETAG;

/**
 * Created on 2018/01/7 by nora.
 **/

public class CraftParticleLine {

    private static float offset = 0.40f;
    private CraftParticle manager;
    private long entityId;
    private String text;
    private int index;
    private Boolean disabled = false;
    private Boolean update = false;
    private int delay = 1000;
    private long lastUpdateTime;
    private String previousText;

    CraftParticleLine(CraftParticle manager, String text, int index) {
        this.manager = manager;
        this.entityId = Entity.entityCount++;
        this.setText(text);
        this.index = index;
    }

    public String getText() {
        return text;
    }

    private void setText(String text) {
        this.text = text;
        this.update = false;
        Scanner sc = new Scanner(text);
        for (String s; (s = sc.findWithinHorizon("(?<=%).*?(?=%)", 0)) != null; ) {
            for (Map.Entry<String, PlaceholderHook> placeholder : PlaceholderAPI.getPlaceholders().entrySet()) {
                if (placeholder.getValue().onPlaceholderRequest(null, s) == null) {
                    this.setUpdate();
                    return;
                }
                if (!placeholder.getValue().onPlaceholderRequest(null, s).equals(s))
                    this.setUpdate();
            }
        }
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public int getDelay() {
        return delay;
    }

    private void setUpdate() {
        this.update = true;
    }

    public Boolean getUpdate() {
        return this.update;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public RemoveEntityPacket removeLine() {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = this.entityId;
        return pk;

    }

    public void updateLines() {
        if (!this.getDisabled() && this.getUpdate() && this.getText() != this.previousText) {
            String name = this.getText();
            for (Player player : this.manager.getLevel().getPlayers().values()) {
                Scanner sc = new Scanner(name);
                for (String s; (s = sc.findWithinHorizon("(?<=%).*?(?=%)", 0)) != null; ) {
                    for (Map.Entry<String, PlaceholderHook> placeholder : PlaceholderAPI.getPlaceholders().entrySet()) {
                        name = name.replace("%" + s + "%", placeholder.getValue().onPlaceholderRequest(player, s));
                    }
                }
                this.lastUpdateTime = System.currentTimeMillis();
                SetEntityDataPacket pk = new SetEntityDataPacket();
                pk.eid = this.entityId;
                pk.metadata = new EntityMetadata().put(new StringEntityData(DATA_NAMETAG, TextFormat.colorize(name)));
                player.dataPacket(pk);
            }
            this.previousText = name;
        }
    }

    public void sendLine(Collection<Player> values) {
        String name = this.getText();
        for (Player player : values) {
            Scanner sc = new Scanner(name);
            for (String s; (s = sc.findWithinHorizon("(?<=%).*?(?=%)", 0)) != null; ) {
                for (Map.Entry<String, PlaceholderHook> placeholder : PlaceholderAPI.getPlaceholders().entrySet()) {
                    name = name.replace("%" + s + "%", placeholder.getValue().onPlaceholderRequest(player, s));
                }
            }
            this.lastUpdateTime = System.currentTimeMillis();

            AddPlayerPacket pk = new AddPlayerPacket();
            pk.uuid = UUID.randomUUID();
            pk.username = "";
            pk.entityUniqueId = this.entityId;
            pk.entityRuntimeId = this.entityId;
            pk.x = manager.getX();
            pk.y = manager.getY() - offset * index;
            pk.z = manager.getZ();
            pk.speedX = 0;
            pk.speedY = 0;
            pk.speedZ = 0;
            pk.yaw = 0;
            pk.pitch = 0;
            long flags = (
                    (1L << Entity.DATA_FLAG_CAN_SHOW_NAMETAG) |
                            (1L << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG) |
                            (1L << Entity.DATA_FLAG_IMMOBILE)
            );
            pk.metadata = new EntityMetadata()
                    .putLong(Entity.DATA_FLAGS, flags)
                    .putString(DATA_NAMETAG, TextFormat.colorize(name))
                    .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                    .putFloat(Entity.DATA_SCALE, 0.00f); //zero causes problems on debug builds?
            pk.item = Item.get(Item.AIR);
            player.dataPacket(pk);
        }
        this.previousText = name;
    }

    public RemoveEntityPacket delLine() {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = this.entityId;

        return pk;
    }
}