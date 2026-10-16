package com.example.kaomojichat;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

/**
 * 聊天颜文字前缀模组（客户端）。
 *
 * 原理：通过 Mixin 拦截 ChatScreen.sendMessage，
 * 在玩家输入的聊天文本发送到服务器之前，自动在前面拼上颜文字前缀。
 */
public class KaomojiChatMod implements ClientModInitializer {

    public static final String MOD_ID = "kaomojichat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        KaomojiConfig.load();
        LOGGER.info("[KaomojiChat] 已加载，当前前缀: {}", KaomojiConfig.getPrefix());

        registerCommands();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("kaomoji")
                    // 设置前缀：/kaomoji set <prefix>
                    .then(literal("set")
                            .then(argument("prefix", StringArgumentType.greedyString())
                                    .executes(context -> {
                                        String prefix = StringArgumentType.getString(context, "prefix");
                                        KaomojiConfig.setPrefix(prefix);
                                        context.getSource().sendFeedback(
                                                Component.literal("§a[KaomojiChat]§r 前缀已设置为: " + prefix));
                                        return 1;
                                    })))
                    // 查看当前前缀：/kaomoji get
                    .then(literal("get")
                            .executes(context -> {
                                context.getSource().sendFeedback(
                                        Component.literal("§b[KaomojiChat]§r 当前前缀: " + KaomojiConfig.getPrefix()));
                                return 1;
                            }))
                    // 开启/关闭：/kaomoji toggle [true|false]
                    .then(literal("toggle")
                            .executes(context -> {
                                boolean next = !KaomojiConfig.isEnabled();
                                KaomojiConfig.setEnabled(next);
                                String state = next ? "§a开启" : "§c关闭";
                                context.getSource().sendFeedback(
                                        Component.literal("§b[KaomojiChat]§r 颜文字前缀已" + state));
                                return 1;
                            })
                            .then(argument("enabled", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                        KaomojiConfig.setEnabled(enabled);
                                        String state = enabled ? "§a开启" : "§c关闭";
                                        context.getSource().sendFeedback(
                                                Component.literal("§b[KaomojiChat]§r 颜文字前缀已" + state));
                                        return 1;
                                    })))
                    // 恢复默认前缀：/kaomoji reset
                    .then(literal("reset")
                            .executes(context -> {
                                KaomojiConfig.setPrefix(KaomojiConfig.DEFAULT_PREFIX);
                                KaomojiConfig.setEnabled(true);
                                context.getSource().sendFeedback(
                                        Component.literal("§a[KaomojiChat]§r 已恢复默认前缀: " + KaomojiConfig.DEFAULT_PREFIX));
                                return 1;
                            })));
        });
    }
}
