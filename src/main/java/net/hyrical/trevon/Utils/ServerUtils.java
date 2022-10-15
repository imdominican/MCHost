package net.hyrical.trevon.Utils;

import net.hyrical.trevon.Data.FileManager;
import net.hyrical.trevon.JSON.ServerMeta;
import net.hyrical.trevon.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class ServerUtils {
    private final DataBase dataBase;
    private final FileManager fileManager;
    private final Main main = Main.getServerCore();

    public void createServer(ProxiedPlayer player, String name) {
        ArrayList<ServerMeta> data = dataBase.getServerInfo(name);

        if (data.size() == 0) {

            File folder = new File(fileManager.getServerFolder() + "/" + name);

            folder.mkdir();

            Path original = fileManager.getPaperJar().toPath();
            Path copied = new File(folder, "paper.jar").toPath();

            try {
                Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(fileManager.getServerProperties().toPath(), new File(folder, "server.properties").toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(fileManager.getSpigotyml().toPath() , new File(folder, "spigot.yml").toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Process process = null;
            int port = findOpenPort();
            try {
                process = Runtime.getRuntime().exec("java -Xms2G -Xmx2G -Dcom.mojang.eula.agree=true -jar paper.jar --nogui --max-players 10 -p " + port, null,  folder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            main.addProcess(process, name);

            ServerInfo serverInfo = main.getProxy().constructServerInfo(name, new InetSocketAddress("localhost", port), "A MCHost Player Server", false);

            ProxyServer.getInstance().getServers().put(name, serverInfo);

            dataBase.insertServerInfo(new ServerMeta(10, name, player.getUniqueId(), folder.getPath()));

        }
        else {
            //msg err
        }


    }

    public void startServer(ProxiedPlayer player, String name) {

        main.getProxy().getScheduler().runAsync(main, () -> { //Async to stop it from running on the main thread if the request takes time

            ArrayList<ServerMeta> serverMetas = dataBase.getServerInfo(name);

            if (serverMetas.size() == 0) {
                TextComponent textComponent = new TextComponent("Server does not exist");
                textComponent.setColor(ChatColor.RED);
                player.sendMessage(textComponent);
            }
            else {
                if (!main.getProcesses().containsKey(name)) {

                    ServerMeta serverMeta = serverMetas.get(0);

                    File folder = new File(serverMeta.getPath());

                    if (!folder.exists()) {
                        try {
                            throw new FileNotFoundException("Database and server files are out of sync. Server " + name + " exists in the database but not in servers folder");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    int port = findOpenPort();
                    Process process = null;
                    try {
                        process = Runtime.getRuntime().exec("java -Xms2G -Xmx2G -Dcom.mojang.eula.agree=true -jar paper.jar --nogui --max-players 10 -p " + port, null,  folder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    main.addProcess(process, name);
                    ProxyServer.getInstance().getServers().put(name, main.getProxy().constructServerInfo(name, new InetSocketAddress("localhost", port), "idk", false));
                    TextComponent textComponent = new TextComponent("Server is starting!");
                    textComponent.setColor(ChatColor.GREEN);
                    player.sendMessage(textComponent);

                }
                else {
                    TextComponent textComponent = new TextComponent("Server is already running. Use /server " + name + " to join the server!");
                    textComponent.setColor(ChatColor.RED);
                    player.sendMessage(textComponent);
                }
            }
        });

    }

    public ServerUtils(DataBase dataBase, FileManager fileManager) {
        this.dataBase = dataBase;
        this.fileManager = fileManager;
    }

    private int findOpenPort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {

            return serverSocket.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 5667;
    }



}
