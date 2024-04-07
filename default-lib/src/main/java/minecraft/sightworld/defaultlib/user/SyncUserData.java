package minecraft.sightworld.defaultlib.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SyncUserData {

    private String user;

    private String field;

    private Object value;

}
