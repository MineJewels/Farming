package org.minejewels.jewelsfarming;

import lombok.Getter;
import net.abyssdev.abysslib.config.AbyssConfig;
import net.abyssdev.abysslib.patterns.registry.Registry;
import net.abyssdev.abysslib.plugin.AbyssPlugin;
import net.abyssdev.abysslib.runnable.AbyssTask;
import net.abyssdev.abysslib.text.MessageCache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.minejewels.jewelsfarming.crop.Crop;
import org.minejewels.jewelsfarming.crop.registry.CropRegistry;
import org.minejewels.jewelsfarming.crop.task.ResetTask;
import org.minejewels.jewelsfarming.listeners.BreakListener;
import org.minejewels.jewelsfarming.listeners.PlaceListener;

@Getter
public final class JewelsFarming extends AbyssPlugin {

    private static JewelsFarming api;

    private final AbyssConfig settingsConfig = this.getAbyssConfig("settings");
    private final AbyssConfig langConfig = this.getAbyssConfig("lang");

    private final MessageCache messageCache = new MessageCache(this.langConfig);

    private final Registry<Material, Crop> cropRegistry = new CropRegistry();
    private ResetTask resetTask;

    @Override
    public void onEnable() {
        JewelsFarming.api = this;

        this.loadMessages(this.messageCache, this.langConfig);
        this.loadCrops();

        this.resetTask = new ResetTask(this);

        new BreakListener(this);
        new PlaceListener(this);
    }

    @Override
    public void onDisable() {
        this.resetTask.run();
    }

    private void loadCrops() {
        for (final String crop : this.settingsConfig.getSectionKeys("crops")) {
            this.cropRegistry.register(Material.matchMaterial(crop.toUpperCase()), new Crop(this, crop));
        }
    }

    public static JewelsFarming get() {
        return JewelsFarming.api;
    }
}
