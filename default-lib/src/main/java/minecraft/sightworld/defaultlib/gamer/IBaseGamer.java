package minecraft.sightworld.defaultlib.gamer;

import it.unimi.dsi.fastutil.ints.IntSet;
import minecraft.sightworld.defaultlib.gamer.constants.JoinMessage;
import minecraft.sightworld.defaultlib.gamer.friends.Friend;
import minecraft.sightworld.defaultlib.gamer.friends.FriendAction;
import minecraft.sightworld.defaultlib.gamer.section.Section;
import minecraft.sightworld.defaultlib.group.Group;

import java.util.List;
import java.util.Map;

public interface IBaseGamer {

    int getPlayerID();

    String getName();

    void setName(String name);

    String getDisplayName();


    String getPrefix();

    void setPrefix(String prefix, boolean saved);

    Group getGroup();

    void setGroup(Group group);

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

    int getRuby();


    void setDefaultJoinMessage(JoinMessage message);

    void addJoinMessage(JoinMessage message);

    JoinMessage getJoinMessage();

    List<JoinMessage> getJoinMessages();

    void setDiscordID(int value);

    int getDiscordID();

    boolean isFriend(Friend friend);

    void changeFriend(FriendAction action, Friend friend);

    IntSet getFriends();


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
