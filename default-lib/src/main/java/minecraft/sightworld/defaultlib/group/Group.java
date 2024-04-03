package minecraft.sightworld.defaultlib.group;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum Group {
    DEFAULT(189, 0, "§7Игрок", "§7Default", "default", "§7", "§8 »§7", 0),

    SLIME(179, 100, "§a§lSLIME", "§a§lSLIME", "slime", "§a§lSLIME §a", "§8 »§7", 1),
    STRAY(178, 200, "§5§lSTRAY", "§e§lAKIO", "stray", "§e§lAKIO §e", "§8 »§7", 2),
    BLAZE(9, 300, "§b§lTRIVAL", "§b§lTRIVAL", "blaze", "§b§lTRIVAL §b", "§8 »§7", 3),
    WITHER(8, 400, "§c§lAXSIDE", "§c§lAXSIDE", "wither", "§c§lAXSIDE §c", "§8 »§7", 4),

    MEDIA(7, 500, "§6§lMEDIA", "§6§lMEDIA", "media", "§6§lMEDIA §6", "§8 »§f", 5),

    BUILDER(6, 650, "§3Строитель", "§3§lBUILDER", "builder", "§3§lBUILDER §3", "§8 »§f", 6),
    SRBUILDER(5, 660, "§3Ст. Строитель", "§3§lSR. BUILDER", "srbuilder", "§3§lSR. BUILDER §3", "§8 »§f", 7),

    GUARD(4, 680, "§2§lМл. Хелпер §r§7(§2§lGUARD§r§7)", "§2§lGUARD", "guard", "§2§lGUARD §2", "§8 »§f", 8),
    HELPER(3, 700, "§9Хелпер", "§2§lHELPER", "helper", "§2§lHELPER §2", "§8 »§f", 9),
    MODERATOR(2, 750, "§9Модератор", "§9§lMODERATOR", "moderator", "§9§lMODER §9", "§8 »§f", 10),

    ADMIN(1, 900, "§4Админ", "§4§lADMIN", "administrator", "§4§lADMIN §4", "§8 »§f", 11),
    OWNER(0, 1000, "§4Владелец", "§4§lOWNER", "owner", ADMIN.getPrefix(), ADMIN.getSuffix(), 100),
    DEVELOPER(0, 1100, "§6Разработчик", "§6§lDEV", "developer", ADMIN.getPrefix(), ADMIN.getSuffix(), 110);

    int priority, id;
    String name, nameEn, groupName, prefix, suffix;
    int level;


    public static final Int2ObjectMap<Group> GROUPS = Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>());

    public static Group getGroup(final int groupID) {
        val group = GROUPS.get(groupID);
        if (group != null) {
            return group;
        }

        return DEFAULT;
    }

    public static Group getGroupByLevel(final int level) {
        return GROUPS.values().stream()
                .filter(g -> g.getLevel() == level)
                .findFirst()
                .orElse(Group.DEFAULT);
    }

    public static Group getGroupByName(final @NotNull String name) {
        return GROUPS.values().stream()
                .filter(g -> g.getGroupName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(Group.DEFAULT);
    }

    public static Group getGroupById(final int id) {
        return GROUPS.values().stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(Group.DEFAULT);
    }

    public static Group getNextGroup(final @NotNull Group group, final int amount) {
        int find = group.ordinal() + amount;
        if (find + 1 > GROUPS.size()) {
            return group;
        }

        return GROUPS.values().stream()
                .filter(g2 -> g2.ordinal() == find)
                .findFirst()
                .orElse(group);
    }

    public static Group getNextGroup(final @NotNull Group group) {
        return Group.getNextGroup(group, 1);
    }

    @Override
    public String toString() {
        return nameEn;
    }

    static {
        for (val group : values()) {
            GROUPS.put(group.getId(), group);
        }
    }
}