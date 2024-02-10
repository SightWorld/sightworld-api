package minecraft.sightworld.defaultlib.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public String fixLength(int limit, String string) {
        return string.length() > limit ? string.substring(0, limit) : string;
    }
}
