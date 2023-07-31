package org.minejewels.jewelsfarming.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.minejewels.jewelsfarming.crop.Crop;

@Getter
@Setter
public class PlayerFarmEvent extends Event implements Cancellable {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final Crop crop;
    private final BlockBreakEvent event;
    private boolean cancelled;

    public PlayerFarmEvent(final Player player, final Crop crop, final BlockBreakEvent event) {
        this.player = player;
        this.crop = crop;
        this.event = event;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
