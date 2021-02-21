package net.minestom.script.handler;

import net.minestom.server.utils.Vector;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegionHandler {

    private Map<String, Region> regionMap = new ConcurrentHashMap<>();

    protected RegionHandler() {

    }

    public Region createRegion(String identifier, Vector minPos, Vector maxPos, NBTCompound nbtCompound) {
        if (regionMap.containsKey(identifier)) {
            return null;
        }
        Region region = new Region(identifier, minPos, maxPos, nbtCompound);
        this.regionMap.put(identifier, region);
        return region;
    }

    public Region getRegion(String identifier) {
        return regionMap.get(identifier);
    }

    public static class Region {
        private String identifier;
        private Vector minPos, maxPos;
        private NBTCompound nbtCompound;

        protected Region(String identifier, Vector minPos, Vector maxPos, NBTCompound nbtCompound) {
            this.identifier = identifier;
            this.minPos = minPos;
            this.maxPos = maxPos;
            this.nbtCompound = nbtCompound;
        }

        public String getIdentifier() {
            return identifier;
        }

        public Vector getMinPos() {
            return minPos;
        }

        public Vector getMaxPos() {
            return maxPos;
        }

        public NBTCompound getNbtCompound() {
            return nbtCompound;
        }
    }

}
