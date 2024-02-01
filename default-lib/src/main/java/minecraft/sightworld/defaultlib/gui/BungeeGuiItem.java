package minecraft.sightworld.defaultlib.gui;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import minecraft.sightworld.defaultlib.item.SItem;

import java.util.function.BiConsumer;

@Getter
@Setter
@RequiredArgsConstructor
public class BungeeGuiItem {

    private String uuid;

    private final SItem item;

    private int slot;

    private final transient BiConsumer<String, String> onClick;

    private String clickType;

    private boolean autoClosable = true;

}
