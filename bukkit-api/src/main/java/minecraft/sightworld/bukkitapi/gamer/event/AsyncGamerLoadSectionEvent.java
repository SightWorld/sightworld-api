package minecraft.sightworld.bukkitapi.gamer.event;

import lombok.Getter;
import minecraft.sightworld.bukkitapi.gamer.entity.BukkitGamer;
import minecraft.sightworld.defaultlib.gamer.section.Section;

import java.util.HashSet;
import java.util.Set;

public class AsyncGamerLoadSectionEvent extends GamerEvent {

    @Getter
    private final Set<Class<? extends Section>> sections = new HashSet<>();

    public AsyncGamerLoadSectionEvent(BukkitGamer gamer) {
        super(gamer, true);
    }

    public void setSections(Set<Class<? extends Section>> sections) {
        this.sections.addAll(sections);
    }
}
