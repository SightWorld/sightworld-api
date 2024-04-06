package minecraft.sightworld.defaultlib.user;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.defaultlib.user.session.UserSession;

@Getter
@Setter
@DatabaseTable(tableName = "users")
public class UserData {
    public UserData() {

    }

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @DatabaseField
    private String prefix;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<UserSession> sessions;

    private UserSession activeSession;


}
