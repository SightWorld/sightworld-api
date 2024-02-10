package minecraft.sightworld.defaultlib.utils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Represents a traditional int field enum.
 * В буквальности заменяет com/comphenix/protocol/reflect/IntEnum.java
 *
 * @author Kristian
 */
public class IntEnum {

    // Used to convert between IDs and names
    protected BiMap<Integer, String> members = HashBiMap.create();

    /**
     * Registers every declared integer field.
     */
    public IntEnum() {
        registerAll();
    }

    /**
     * Registers every public int field as a member.
     */
    protected void registerAll() {
        try {
            // Register every int field
            for (Field entry : this.getClass().getFields()) {
                if (entry.getType().equals(int.class)) {
                    registerMember(entry.getInt(this), entry.getName());
                }
            }

        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a member.
     * @param id - id of member.
     * @param name - name of member.
     */
    protected void registerMember(int id, String name) {
        members.put(id, name);
    }

    /**
     * Determines whether or not the given member exists.
     * @param id - the ID of the member to find.
     * @return TRUE if a member with the given ID exists, FALSE otherwise.
     */
    public boolean hasMember(int id) {
        return members.containsKey(id);
    }

    /**
     * Retrieve the ID of the member with the given name.
     * @param name - name of member to retrieve.
     * @return ID of the member, or NULL if not found.
     */
    public Integer valueOf(String name) {
        return members.inverse().get(name);
    }

    /**
     * Retrieve the name of the member with the given id.
     * @param id - id of the member to retrieve.
     * @return Declared name of the member, or NULL if not found.
     */
    public String getDeclaredName(Integer id) {
        return members.get(id);
    }

    /**
     * Retrieve the ID of every registered member.
     * @return Enumeration of every value.
     */
    public Set<Integer> values() {
        return new HashSet<Integer>(members.keySet());
    }
}