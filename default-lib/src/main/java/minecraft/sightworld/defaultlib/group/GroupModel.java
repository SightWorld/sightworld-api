package minecraft.sightworld.defaultlib.group;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupModel {

    Group groupType;
    List<Permission> permissions;

    public boolean isStaff() {
        return getGroupType().getLevel() >= Group.GUARD.getLevel();
    }

    public boolean isDonate() {

        return getGroupType().getLevel() >= Group.SLIME.getLevel();
    }

    @Override
    public String toString() {
        return groupType.getNameEn();
    }
}