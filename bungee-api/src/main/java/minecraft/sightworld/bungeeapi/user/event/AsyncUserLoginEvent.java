package minecraft.sightworld.bungeeapi.user.event;

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
public class AsyncUserLoginEvent extends AsyncEvent<AsyncUserLoginEvent> implements Cancellable {

    ProxiedUser user;
    PendingConnection connection;

    @NonFinal
    boolean cancelled;
    @NonFinal
    BaseComponent cancelReason;

    public AsyncUserLoginEvent(
            final ProxiedUser user,
            final PendingConnection connection,
            final Callback<AsyncUserLoginEvent> done
    ) {
        super(done);
        this.user = user;
        this.connection = connection;
    }

}
