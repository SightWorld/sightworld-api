package minecraft.sightworld.bukkitapi.event;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SightEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected SightEvent(boolean async) {
        super(async);
    }

    protected SightEvent() {

    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }


    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
