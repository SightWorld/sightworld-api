package minecraft.sightworld.defaultlib.gamer.section;

import lombok.Getter;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.gamer.IBaseGamerImpl;

public abstract class Section {

    @Getter
    protected final IBaseGamerImpl baseGamer;

    public Section(IBaseGamer baseGamer) {
        this.baseGamer = (IBaseGamerImpl) baseGamer;
    }

    public abstract boolean loadData();
}
