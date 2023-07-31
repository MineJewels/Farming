package org.minejewels.jewelsfarming.listeners;

import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.scheduler.AbyssScheduler;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.minejewels.jewelsfarming.JewelsFarming;
import org.minejewels.jewelsfarming.crop.Crop;
import org.minejewels.jewelsfarming.events.PlayerFarmEvent;
import org.minejewels.jewelslevels.JewelsLevels;

import java.util.List;

public class BreakListener extends AbyssListener<JewelsFarming> {

    private final List<String> validWorlds;
    private final JewelsLevels jewelsLevels;

    public BreakListener(final JewelsFarming plugin) {
        super(plugin);

        this.validWorlds = plugin.getSettingsConfig().getStringList("farm-worlds");
        this.jewelsLevels = JewelsLevels.get();
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {

        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Location location = block.getLocation();
        final World world = location.getWorld();

        if (!this.validWorlds.contains(world.getName())) return;

        final Crop crop = this.plugin.getCropRegistry().getRegistry().get(block.getType());

        if (!this.plugin.getCropRegistry().containsKey(block.getType())) {

            if (player.hasPermission("farming.admin")) return;

            this.plugin.getMessageCache().sendMessage(player, "messages.cannot-break");
            event.setCancelled(true);
            return;
        }

        if (!NBTUtils.get().contains(player.getInventory().getItemInMainHand(), "HARVESTER-HOE")) {
            this.plugin.getMessageCache().sendMessage(player, "messages.hoe-required");
            event.setCancelled(true);
            return;
        }

        if (this.jewelsLevels.getPlayerStorage().get(player.getUniqueId()).getLevel() < crop.getLevelRequired()) {
            this.plugin.getMessageCache().sendMessage(player, "messages.level-too-low", new PlaceholderReplacer()
                    .addPlaceholder("%crop%", crop.getCrop().toLowerCase())
                    .addPlaceholder("%level%", Utils.format(crop.getLevelRequired())));

            event.setCancelled(true);
            return;
        }

        event.setDropItems(false);

        final PlayerFarmEvent farmEvent = new PlayerFarmEvent(player, crop, event);

        Events.call(farmEvent);

        if (farmEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        this.plugin.getResetTask().add(crop, location);
    }
}
