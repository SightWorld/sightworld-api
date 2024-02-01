package minecraft.sightworld.defaultlib.item;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SItem {

    private final SMaterial material;

    private String title = "";
    private String headTexture = null;

    private List<String> lore = new ArrayList<>();

    private boolean glow = false;

    private int amount = 1;

    public SItem(SMaterial material) {
        this.material = material;
    }

}
