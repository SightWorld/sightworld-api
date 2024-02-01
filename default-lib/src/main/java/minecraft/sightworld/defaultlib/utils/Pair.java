package minecraft.sightworld.defaultlib.utils;

import lombok.Getter;

@Getter
public class Pair<A, B> {

    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
}
