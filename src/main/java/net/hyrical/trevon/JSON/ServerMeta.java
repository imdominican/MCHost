package net.hyrical.trevon.JSON;

import java.util.UUID;

public class ServerMeta {

    int slots;

    String name;

    UUID uuid;

    String path;

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ServerMeta(int slots, String name, UUID uuid, String path) {
        this.slots = slots;
        this.name = name;
        this.uuid = uuid;
        this.path = path;
    }

}
