package minecraft.sightworld.defaultlib.gui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class BungeeGuiDto {
    
    private final String title;
    
    private final int size;
    private List<BungeeGuiItem> items = new ArrayList<>();

    private List<Integer> markup = new ArrayList<>();

}
