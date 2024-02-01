package minecraft.sightworld.defaultlib.gamer;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import minecraft.sightworld.defaultlib.gamer.constants.JoinMessage;
import minecraft.sightworld.defaultlib.gamer.section.*;
import minecraft.sightworld.defaultlib.manager.VanishManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public abstract class IBaseGamerImpl implements IBaseGamer {

    private static final Set<String> DEVELOPERS = ImmutableSet.of(
            "Dwyur"
    );

    /**
     * Проверить, игрок красный админ или нет
     * p.s красный админ: человек, обладающих всеми правами.
     * @return - возвращает да/нет.
     */
    public boolean isRedAdmin() {
        return DEVELOPERS.contains(getName());
    }


    private final Map<String, Object> data = new HashMap<>();
    private final Map<String, Section> sections = new HashMap<>();

    @Setter
    protected String name;

    private final long start; // для дебага

    IBaseGamerImpl(String name) {
        this.name = name;
        this.start = System.currentTimeMillis();

        initSection(BaseSection.class);

        if (initSections() != null) {
            initSections().forEach(this::initSection);
        }
    }

    protected Set<Class<? extends Section>> initSections() {
        return Set.of(MoneySection.class, FriendSection.class);
    }

    public final <T extends Section> void initSection(Class<T> clazz) {
        T section = null;
        try {
            section = clazz.getConstructor(IBaseGamer.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (section == null) {
            throw new IllegalArgumentException("ERROR, секция - "
                    + clazz.getSimpleName() + ", ник игрока - " + name);
        }

        val nameSection = clazz.getSimpleName();
        sections.put(nameSection, section);
    }

    public final long getStart() {
        return start;
    }

    @Override
    public String getDisplayName() {
        return getPrefix() + getName() + getTag();
    }


    @Override
    public int getPlayerID() {
        return getSection(BaseSection.class).getPlayerID();
    }

    public JoinMessage getJoinMessage() {
        return getSection(JoinMessageSection.class).getJoinMessage();
    }
    public void setDefaultJoinMessage(JoinMessage joinMessage) {
        getSection(JoinMessageSection.class).setDefaultJoinMessage(joinMessage, true);
    }

    @Override
    public String getPrefix() {
        return getSection(BaseSection.class).getPrefix();
    }

    @Override
    public void setPrefix(String prefix, boolean saved) {
        if (saved) {
            getSection(BaseSection.class).savePrefix(prefix);
        } else {
            getSection(BaseSection.class).setPrefix(prefix);
        }
    }

    @Override
    public String getTag() {
        return getSection(BaseSection.class).getTag();
    }

    @Override
    public void setTag(String tag, boolean saved) {
        if (saved) {
            getSection(BaseSection.class).saveTag(tag);
        } else {
            getSection(BaseSection.class).setTag(tag);
        }
    }

    @Override
    public void addData(String name, Object data) {
        this.data.put(name.toLowerCase(), data);
    }

    @Override
    public <T> T getData(String name) {
        return (T) this.data.get(name.toLowerCase());
    }

    @Override
    public void clearData(String name) {
        this.data.remove(name.toLowerCase());
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public Map<String, Section> getSections() {
        return sections;
    }

    @Override
    public void changeRuby(int value) {
        getSection(MoneySection.class).changeRuby(value);
    }

    @Override
    public void setRuby(int value) {
        getSection(MoneySection.class).setRuby(value);
    }
}
