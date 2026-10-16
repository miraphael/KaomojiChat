package com.example.kaomojichat.mixin;

import com.example.kaomojichat.KaomojiConfig;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * 注入 ChatScreen.handleChatInput，在玩家输入框的内容交给原版逻辑之前，
 * 自动在前面拼上配置好的颜文字前缀。
 */
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @ModifyVariable(
            method = "handleChatInput(Ljava/lang/String;Z)V",
            at = @At("HEAD"),
            ordinal = 0
    )
    private String kaomojichat$addPrefix(String chatText) {
        // 未启用则不处理
        if (!KaomojiConfig.isEnabled()) {
            return chatText;
        }
        // 空文本不处理
        if (chatText == null || chatText.isEmpty()) {
            return chatText;
        }
        // 命令（以 / 开头）不加前缀，避免破坏指令
        if (chatText.startsWith("/")) {
            return chatText;
        }
        String prefix = KaomojiConfig.getPrefix();
        if (prefix == null || prefix.isEmpty()) {
            return chatText;
        }
        // 已经以前缀开头则不重复添加
        if (chatText.startsWith(prefix)) {
            return chatText;
        }
        return prefix + chatText;
    }
}
