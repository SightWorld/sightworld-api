package minecraft.sightworld.bungeeapi.gamer.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import minecraft.sightworld.bungeeapi.user.ProxiedUser;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.AsyncEvent;
import net.md_5.bungee.api.plugin.Cancellable;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@Getter
@Setter
@ToString
public class AsyncGamerLoginEvent extends AsyncEvent<AsyncGamerLoginEvent> implements Cancellable {

    ProxiedUser user;
    PendingConnection connection;

    @NonFinal
    boolean cancelled;
    @NonFinal
    BaseComponent cancelReason;

    public AsyncGamerLoginEvent(
            final ProxiedUser user,
            final PendingConnection connection,
            final Callback<AsyncGamerLoginEvent> done
    ) {
        super(done);
        this.user = user;
        this.connection = connection;
    }

}
