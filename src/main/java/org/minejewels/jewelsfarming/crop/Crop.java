package org.minejewels.jewelsfarming.crop;

import lombok.Data;
import org.bukkit.Material;
import org.minejewels.jewelsfarming.JewelsFarming;

@Data
public class Crop {

    private final String crop;
    private final Material material;
    private final int experiencePer, levelRequired;

    public Crop(final JewelsFarming plugin, final String cropName) {
        this.crop = cropName;
        this.material = Material.matchMaterial(plugin.getSettingsConfig().getString("crops." + cropName + ".material"));
        this.experiencePer = plugin.getSettingsConfig().getInt("crops." + cropName + ".experience-per");
        this.levelRequired = plugin.getSettingsConfig().getInt("crops." + cropName + ".level-required");
    }
}
