package minecraft.sightworld.bungeeapi.gui.test;

import minecraft.sightworld.bungeeapi.gui.BungeeGui;
import minecraft.sightworld.defaultlib.gui.BungeeGuiItem;
import minecraft.sightworld.defaultlib.item.SItem;
import minecraft.sightworld.defaultlib.item.SMaterial;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TestGui extends BungeeGui {

    private final ProxiedPlayer player;

    public TestGui(ProxiedPlayer player) {
        super(player, "Тестовое меню", 5);
        this.player = player;
        setDefaultMarkupItems();
        //setUpdater(20);
        draw();
        open();
    }

    @Override
    public void draw() {
        for (int i = 1; i < 100; i++) {
            int finalI = i;
            SItem item = new SItem(SMaterial.DIAMOND);
            item.setTitle("Предмет #" + i);
            item.setAmount(i); //кол-во предмета
            item.setGlow(true); //светится айтем

            //Можно сразу bungee gui item добавить
            BungeeGuiItem guiItem = new BungeeGuiItem(item, (p, clickType) -> {
                player.sendMessage("Ты нажал на предмет номер: " + finalI);
            });

            guiItem.setAutoClosable(true); //будет ли закрываться меню после нажатия на этот предмет
            addItem(guiItem);
//
//            addItem(item, (p, clickType) -> {
//                player.sendMessage("Ты нажал на предмет номер: " + finalI);
//            });
        }
    }
}
