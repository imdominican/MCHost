package net.hyrical.trevon.Data;

import net.hyrical.trevon.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class FileManager {
    private final Main main;
    private final File paperJar;
    private final File serverFolder;
    private final File spigotyml;
    private final File serverProperties;
    private Configuration config;


    public FileManager(Main main) throws Exception {
        this.main = main;
        File dataFolder = main.getDataFolder();

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File jarDir = new File(dataFolder.getPath() + "/jar");

        if (!jarDir.exists()) {
            jarDir.mkdir();
        }

        File jar = new File(jarDir.getPath(), "paper.jar");

        if (!jar.exists()) {
            throw new FileNotFoundException("Did not find " + jar.getAbsolutePath());
        }
        paperJar = jar;

        File server = new File(dataFolder + "/servers");

        if (!server.exists()) {
            server.mkdir();
        }

        File spigotyml = new File(dataFolder, "spigot.yml");

        if (!spigotyml.exists()) {
            throw new FileNotFoundException("Did not find " + spigotyml.getAbsolutePath());
        }

        File serverProperties = new File(dataFolder, "server.properties");

        if (!serverProperties.exists()) {
            throw new FileNotFoundException("Did not find " + serverProperties.getAbsolutePath());
        }

        this.serverProperties = serverProperties;


        this.spigotyml = spigotyml;

        serverFolder = server;

        setupConfig();

    }

    private void setupConfig() {
        if (!main.getDataFolder().exists()) main.getDataFolder().mkdir();
        File file = new File(main.getDataFolder(), "config.yml");
        try {
            if (!file.exists())
                Files.copy(main.getResourceAsStream("config.yml"), file.toPath());

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getPaperJar() {
        return paperJar;
    }

    public File getServerFolder() {
        return serverFolder;
    }

    public Configuration getConfig() {
        return config;
    }

    public File getSpigotyml() {
        return spigotyml;
    }

    public File getServerProperties() {
        return serverProperties;
    }
}
