package org.minejewels.jewelsfarming.crop.task;

import lombok.Getter;
import net.abyssdev.abysslib.runnable.AbyssTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.minejewels.jewelsfarming.JewelsFarming;
import org.minejewels.jewelsfarming.crop.Crop;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class ResetTask extends AbyssTask<JewelsFarming> {

    private final List<Location> cachedLocations = Lists.mutable.empty();
    private final Map<Crop, List<Location>> crops = Maps.mutable.empty();
    private final int resetTime;

    public ResetTask(JewelsFarming plugin) {
        super(plugin);

        for (final Crop crop : plugin.getCropRegistry()) {
            this.crops.put(crop, Lists.mutable.empty());
        }

        this.resetTime = plugin.getSettingsConfig().getInt("reset");

        this.runTaskTimer(plugin, 0L, this.resetTime * 20L);
    }

    public void add(final Crop crop, final Location location) {
        this.crops.get(crop).add(location);
        this.cachedLocations.add(location);
    }

    @Override
    public void run() {

        for (Map.Entry<Crop, List<Location>> cropEntry : this.crops.entrySet()) {
            Crop crop = cropEntry.getKey();
            List<Location> cropLocations = cropEntry.getValue();

            if (cropLocations.isEmpty()) {
                continue;
            }

            for (Location location : cropLocations) {
                location.getBlock().setType(crop.getMaterial());

                BlockData blockData = location.getBlock().getBlockData();
                if (blockData instanceof Ageable) {
                    Ageable ageable = (Ageable) blockData;
                    ageable.setAge(7);
                    location.getBlock().setBlockData(ageable);
                }

                location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.getX() + 0.5, location.getY() + 1.1, location.getZ() + 0.5, 1, 0, 0, 0, 0);
            }

            cropLocations.clear(); // Clear the list of crop locations after resetting them
            this.cachedLocations.clear();
        }
    }
}
