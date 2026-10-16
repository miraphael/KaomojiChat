package com.example.kaomojichat.mixin;

import com.example.kaomojichat.KaomojiConfig;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 注入 ChatScreen.normalizeChatMessage，在玩家发送聊天消息前自动添加颜文字前缀。
 *
 * 26.1+ 引入了 normalizeChatMessage 方法，替代了旧的 sendMessage，
 * 所有聊天消息（包括命令）都会经过此方法处理后发送。
 */
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @Inject(
            method = "normalizeChatMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void kaomojichat$addPrefix(String message, CallbackInfoReturnable<String> cir) {
        // 未启用则不处理
        if (!KaomojiConfig.isEnabled()) {
            return;
        }
        // 空文本不处理
        if (message == null || message.isEmpty()) {
            return;
        }
        // 命令（以 / 开头）不加前缀，避免破坏指令
        if (message.startsWith("/")) {
            return;
        }
        String prefix = KaomojiConfig.getPrefix();
        if (prefix == null || prefix.isEmpty()) {
            return;
        }
        // 已经以前缀开头则不重复添加
        if (message.startsWith(prefix)) {
            return;
        }
        cir.setReturnValue(prefix + message);
    }
}
