package io.armandukx.idletweaks.config;

import java.io.*;

public class Config {
    public boolean bFpsToggle = true;
    public boolean bDistToggle = true;
    public boolean bVolumeToggle = true;

    public int backgroundFps = 10;
    public int backgroundRenderDist = 0;

    private final File configFile = new File("config/idletweaks.config");

    public Config() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            if (!configFile.exists()) {
                saveConfig();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    String key = tokens[0];
                    String value = tokens[1];
                    switch (key) {
                        case "bFpsToggle" -> this.bFpsToggle = Boolean.parseBoolean(value);
                        case "bDistToggle" -> this.bDistToggle = Boolean.parseBoolean(value);
                        case "bVolumeToggle" -> this.bVolumeToggle = Boolean.parseBoolean(value);
                        case "backgroundFps" -> this.backgroundFps = Integer.parseInt(value);
                        case "backgroundRenderDist" -> this.backgroundRenderDist = Integer.parseInt(value);
                        default -> {
                        }
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            writer.write("bFpsToggle=" + bFpsToggle + "\n");
            writer.write("bDistToggle=" + bDistToggle + "\n");
            writer.write("bVolumeToggle=" + bVolumeToggle + "\n");
            writer.write("backgroundFps=" + backgroundFps + "\n");
            writer.write("backgroundRenderDist=" + backgroundRenderDist + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setbFpsToggle(boolean value) {
        if (this.bFpsToggle != value) {
            this.bFpsToggle = value;
            saveConfig();
        }
    }
    public void setbDistToggle(boolean value) {
        if (this.bDistToggle != value) {
            this.bDistToggle = value;
            saveConfig();
        }
    }
    public void setbVolumeToggle(boolean value) {
        if (this.bVolumeToggle != value) {
            this.bVolumeToggle = value;
            saveConfig();
        }
    }

    public void setBackgroundFps(int value) {
        if (this.backgroundFps != value) {
            this.backgroundFps = value;
            saveConfig();
        }
    }
    public void setBackgroundRenderDist(int value) {
        if (this.backgroundRenderDist != value) {
            this.backgroundRenderDist = value;
            saveConfig();
        }
    }
}
