/*
 * IdleTweaks - Enhances performance while Minecraft runs in the background
 * Copyright (c) 2023 Armandukx
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.armandukx.config;

import io.armandukx.IdleTweaks;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    private final static String file = "config/idletweaks.cfg";

    public static void init() {
        config = new Configuration(new File(file));
        try {
            config.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }

    public static String getString(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, "").getString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
        return "";
    }

    public static boolean getBoolean(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, false).getBoolean();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
        return true;
    }

    public static void writeStringConfig(String category, String key, String value) {
        try {
            String set = config.get(category, key, value).getString();
            config.getCategory(category).get(key).set(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }

    public static void writeBooleanConfig(String category, String key, boolean value) {
        try {
            boolean set = config.get(category, key, value).getBoolean();
            config.getCategory(category).get(key).set(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }

    public static boolean hasKey(String category, String key) {
        try {
            if (!config.hasCategory(category)) return false;
            return config.getCategory(category).containsKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
        return false;
    }

    public static String initString(String category, String key, String defaultValue) {
        if (!hasKey(category, key)) {
            writeStringConfig(category, key, defaultValue);
            return defaultValue;
        } else {
            return getString(category, key);
        }
    }

    public static boolean initBoolean(String category, String key, boolean defaultValue) {
        if (!hasKey(category, key)) {
            writeBooleanConfig(category, key, defaultValue);
            return defaultValue;
        } else {
            return getBoolean(category, key);
        }
    }

    public static void reloadConfig() {
        init();

        // General
        IdleTweaks.config.bFpsToggle = initBoolean("general", "bFpsToggle", true);
        IdleTweaks.config.bDistToggle = initBoolean("general", "bDistToggle", false);
        IdleTweaks.config.backgroundFps = initString("general", "backgroundFps", "1");
        IdleTweaks.config.backgroundRenderDist = initString("general", "backgroundRenderDist", "2");
    }
}