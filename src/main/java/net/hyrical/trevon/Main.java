package net.hyrical.trevon;

import net.hyrical.trevon.Commands.CreateCommand;
import net.hyrical.trevon.Commands.StartCommand;
import net.hyrical.trevon.Data.FileManager;
import net.hyrical.trevon.Utils.DataBase;
import net.hyrical.trevon.Utils.ServerUtils;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;

public final class Main extends Plugin {
    private static Main main;


    private final HashMap<String, Process> processes = new HashMap<>();
    private ServerUtils serverUtils;
    private FileManager fileManager;
    private DataBase dataBase;

    public static Main getServerCore() {
        return main;
    }

    @Override
    public void onEnable() {
        main = this;
        getProxy().getPluginManager().registerCommand(this, new CreateCommand(
                "create",
                "mchost.create",
                "createserver"
        ));

        getProxy().getPluginManager().registerCommand(this, new StartCommand(
                "start",
                "mchost.start",
                "startserver"
        ));

        try {
            fileManager = new FileManager(this);
        } catch (Exception e) {
            fileManager = null;
            e.printStackTrace();
            onDisable();
        }

        dataBase = new DataBase(this, fileManager);


        serverUtils = new ServerUtils(dataBase, fileManager);


    }

    @Override
    public void onDisable() {
        endProcesses();
    }

    private void endProcesses() {
        for (Process process : processes.values()) {
            process.destroy();
        }
    }

    public ServerUtils getServerUtils() {
        return serverUtils;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public HashMap<String, Process> getProcesses() {
        return processes;
    }

    public void addProcess(Process process, String name) {
        processes.put(name, process);
    }

}
