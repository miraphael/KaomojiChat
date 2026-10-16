package com.example.kaomojichat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 颜文字前缀配置管理。
 * 配置文件位于 config/kaomojichat.json
 */
public class KaomojiConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("kaomojichat");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("kaomojichat.json");

    /** 默认颜文字前缀 */
    public static final String DEFAULT_PREFIX = "(๑•̀ㅂ•́)و✧ ";

    private static ConfigData data = new ConfigData();

    public static class ConfigData {
        /** 当前使用的颜文字前缀 */
        public String prefix = DEFAULT_PREFIX;
        /** 是否启用前缀 */
        public boolean enabled = true;
    }

    /** 加载配置，若文件不存在则创建默认配置 */
    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                ConfigData loaded = GSON.fromJson(reader, ConfigData.class);
                if (loaded != null) {
                    // 兜底：若配置里 prefix 为空则用默认值
                    if (loaded.prefix == null) loaded.prefix = DEFAULT_PREFIX;
                    data = loaded;
                }
            } catch (IOException e) {
                LOGGER.error("加载 kaomojichat 配置失败，使用默认配置", e);
                data = new ConfigData();
            }
        } else {
            save();
        }
    }

    /** 保存当前配置到磁盘 */
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            LOGGER.error("保存 kaomojichat 配置失败", e);
        }
    }

    public static String getPrefix() {
        return data.prefix;
    }

    public static void setPrefix(String prefix) {
        data.prefix = prefix;
        save();
    }

    public static boolean isEnabled() {
        return data.enabled;
    }

    public static void setEnabled(boolean enabled) {
        data.enabled = enabled;
        save();
    }
}
