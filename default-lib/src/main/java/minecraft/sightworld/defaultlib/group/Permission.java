package minecraft.sightworld.defaultlib.group;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class Permission {
    String perm;
    String context; // контекст (для банжи - сервер, для баккита - ворлд)

    @Override
    public String toString() {
        return "Permission{" +
                "perm='" + perm + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
