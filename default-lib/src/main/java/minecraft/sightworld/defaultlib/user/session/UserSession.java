package minecraft.sightworld.defaultlib.user.session;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.defaultlib.user.UserData;

@Getter
@Setter
@DatabaseTable(tableName = "users_session")
public class UserSession {

    public UserSession() {

    }

    public UserSession(String ip, String server, long startPlay) {
        this.ip = ip;
        this.server = server;
        this.startPlay = startPlay;
    }
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private transient UserData userData;

    @DatabaseField
    private String ip;

    @DatabaseField
    private long played;

    @DatabaseField
    private long lastOnline;

    @DatabaseField
    private String server;

    private long startPlay;

}
