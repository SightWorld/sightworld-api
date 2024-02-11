package minecraft.sightworld.defaultlib.sound;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSound {

    private final SSoundType soundType;
    private final String playerName;

    private int volume = 10;
    private int pitch = 1;

    public SSound(SSoundType soundType, String playerName) {
        this.soundType = soundType;
        this.playerName = playerName;
    }
}
