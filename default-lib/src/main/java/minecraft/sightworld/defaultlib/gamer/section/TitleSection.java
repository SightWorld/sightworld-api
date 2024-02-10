package minecraft.sightworld.defaultlib.gamer.section;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TitleSection extends Section {
    public TitleSection(IBaseGamer baseGamer) {
        super(baseGamer);
    }

    @Override
    public boolean loadData() {
        int playerID = baseGamer.getPlayerID();
        return true;
    }
}
