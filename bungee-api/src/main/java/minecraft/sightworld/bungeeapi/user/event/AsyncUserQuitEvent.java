package minecraft.sightworld.bungeeapi.user.event;

import lombok.AccessLevel;
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

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Setter
@ToString
public final class AsyncUserQuitEvent extends AsyncEvent<AsyncUserQuitEvent> implements Cancellable {
    ProxiedUser user;
    PendingConnection connection;

    @NonFinal
    boolean cancelled;
    @NonFinal
    BaseComponent cancelReason;

    public AsyncUserQuitEvent(
            final ProxiedUser user,
            final PendingConnection connection,
            final Callback<AsyncUserQuitEvent> done
    ) {
        super(done);
        this.user = user;
        this.connection = connection;
    }
}
