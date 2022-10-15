package net.hyrical.trevon.Commands;

import net.hyrical.trevon.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CreateCommand extends Command {

    //private ServerUtils serverUtils = ServerCore.getServerCore().getServerUtils();

    public CreateCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length == 1) {
                Main.getServerCore().getServerUtils().createServer(player, args[0]);
            }
        }
    }
}
