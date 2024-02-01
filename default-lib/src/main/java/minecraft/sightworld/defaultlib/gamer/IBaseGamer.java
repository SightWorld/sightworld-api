package minecraft.sightworld.defaultlib.gamer;

import minecraft.sightworld.defaultlib.gamer.section.Section;

import java.util.Map;

public interface IBaseGamer {

    int getPlayerID();

    String getName();
    void setName(String name);

    String getDisplayName();


    String getPrefix();
    void setPrefix(String prefix, boolean saved);

    String getTag();
    void setTag(String tag, boolean saved);

    void addData(String name, Object data);
    <T> T getData(String name);
    void clearData(String name);
    Map<String, Object> getData();

    boolean isOnline();

    void remove();
    Map<String, Section> getSections();

    boolean hasPermission(String permission);

    default <T extends Section> T getSection(Class<T> sectionClass) {
        return (T) getSections().get(sectionClass.getSimpleName());
    }

    void setRuby(int value);
    void changeRuby(int value);


    default boolean isDonater() {
        return hasPermission("sightworld.donate");
    }

    default boolean isStaff() {
        return hasPermission("sightworld.staff");
    }

    default boolean isAdmin() {
        return hasPermission("sightworld.admin");
    }

    default boolean isWither() {
        return hasPermission("sightworld.wither");
    }

    default boolean isSlime() {
        return hasPermission("sightworld.slime");
    }
}
