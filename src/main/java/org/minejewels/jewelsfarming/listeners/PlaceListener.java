package org.minejewels.jewelsfarming.listeners;

import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.minejewels.jewelsfarming.JewelsFarming;
import org.minejewels.jewelsfarming.crop.Crop;
import org.minejewels.jewelsfarming.events.PlayerFarmEvent;
import org.minejewels.jewelslevels.JewelsLevels;

import java.util.List;

public class PlaceListener extends AbyssListener<JewelsFarming> {

    private final List<String> validWorlds;

    public PlaceListener(final JewelsFarming plugin) {
        super(plugin);

        this.validWorlds = plugin.getSettingsConfig().getStringList("farm-worlds");
    }

    @EventHandler
    public void onBreak(final BlockPlaceEvent event) {

        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Location location = block.getLocation();
        final World world = location.getWorld();

        if (!this.validWorlds.contains(world.getName())) return;
        if (!this.plugin.getResetTask().getCachedLocations().contains(location)) return;

        this.plugin.getMessageCache().sendMessage(player, "messages.cannot-place");
        event.setCancelled(true);
    }
}
