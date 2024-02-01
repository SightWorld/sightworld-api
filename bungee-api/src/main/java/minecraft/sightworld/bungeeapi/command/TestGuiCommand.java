package minecraft.sightworld.bungeeapi.command;

import minecraft.sightworld.bungeeapi.gui.test.TestGui;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TestGuiCommand extends Command {
    public TestGuiCommand() {
        super("testgui", null, "тестгуи");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) sender;
        TestGui testGui = new TestGui(player);
    }
}
