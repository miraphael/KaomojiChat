# Kaomoji Chat Prefix —— 聊天颜文字前缀

一个 Minecraft Java 版 **Fabric 客户端模组**。打字聊天时，自动在你发送的消息前面加上颜文字前缀，再也不用每次手动复制粘贴颜文字了。

默认前缀：`(๑•̀ㅂ•́)و✧ `

## ✨ 特性

- 🎭 **自动加前缀**：发送聊天消息时自动在前面拼上颜文字，服务器上的其他玩家也能看到
- ⚙️ **游戏内自定义**：用命令随时修改前缀、开关功能，无需退出游戏
- 💾 **配置持久化**：设置保存在 `config/kaomojichat.json`，重启不丢失
- 🚫 **不误伤指令**：以 `/` 开头的命令不会被加前缀
- 🔁 **防重复**：消息已经以前缀开头时不会重复添加

## 📦 支持版本

| Minecraft | Fabric Loader | Fabric API | Java | 下载 |
|-----------|---------------|------------|------|------|
| 26.2 | ≥ 0.19.5 | 0.158.0+26.2 | 25 | [kaomojichat-1.0.0+26.2.jar](../../releases/download/v1.0.0+26.2/kaomojichat-1.0.0+26.2.jar) |
| 26.1.2 | ≥ 0.19.5 | 0.158.0+26.1.2 | 25 | [kaomojichat-1.0.0+26.1.2.jar](../../releases/download/v1.0.0+26.1.2/kaomojichat-1.0.0+26.1.2.jar) |
| 1.21.11 | ≥ 0.19.5 | 0.141.6+1.21.11 | 21 | [kaomojichat-1.0.0+1.21.11.jar](../../releases/download/v1.0.0+1.21.11/kaomojichat-1.0.0+1.21.11.jar) |

> 全部版本也可以在 [Releases](../../releases) 页面下载。

## 🎮 使用方法

把对应版本的 jar 放进游戏的 `mods` 文件夹，确保已安装 **Fabric Loader** 和 **Fabric API**，启动游戏即可。

进游戏后直接在聊天框打字发送，消息前面会自动带上颜文字前缀。

### 命令

在游戏聊天框输入：

| 命令 | 说明 |
|------|------|
| `/kaomoji set <前缀>` | 设置颜文字前缀（支持空格和特殊符号） |
| `/kaomoji get` | 查看当前前缀 |
| `/kaomoji toggle` | 开启 / 关闭前缀功能 |
| `/kaomoji toggle true/false` | 直接指定开启或关闭 |
| `/kaomoji reset` | 恢复默认前缀 `(๑•̀ㅂ•́)و✧ ` |

示例：

```
/kaomoji set (≧∇≦)ﾉ 
/kaomoji toggle false
```

### 配置文件

也可以直接编辑 `config/kaomojichat.json`：

```json
{
  "prefix": "(๑•̀ㅂ•́)و✧ ",
  "enabled": true
}
```

## 🛠️ 构建

1. 安装 **JDK 21**（26.x 版本需要 JDK 25），并设置 `JAVA_HOME` 指向 JDK 安装目录（不是 JRE）。
2. 在项目根目录执行：

```bash
# Windows
gradlew.bat build

# macOS / Linux
./gradlew build
```

构建产物位于 `build/libs/kaomojichat-<模组版本>+<MC版本>.jar`。

> 国内用户如果依赖下载慢，`settings.gradle` 和 `build.gradle` 里已经配置了阿里云镜像。

切换到其它游戏版本构建时，只需修改 `gradle.properties` 里的 `minecraft_version` / `fabric_version` / `java_version`。

## 🧠 实现原理

通过 Mixin 注入聊天界面的发送流程，在消息文本交给原版逻辑之前，把它替换成「前缀 + 原文」。
命令（`/xxx`）在注入时会被跳过，因此不会被误加前缀。

这样做的好处：

- 是**客户端**模组，装在自己的客户端即可，服务器无需安装
- 前缀是消息文本的一部分，所以服务器上所有玩家都能看到

## 📁 项目结构

```
.
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradle/wrapper/
├── gradlew / gradlew.bat
└── src/main/
    ├── java/com/example/kaomojichat/
    │   ├── KaomojiChatMod.java        # 客户端入口 + 命令注册
    │   ├── KaomojiConfig.java         # 配置读写
    │   └── mixin/
    │       └── ChatScreenMixin.java   # 聊天前缀注入
    └── resources/
        ├── fabric.mod.json
        ├── kaomojichat.mixins.json
        └── assets/kaomojichat/
            ├── icon.png
            └── lang/
                ├── en_us.json
                └── zh_cn.json
```

## ⚠️ 常见问题

**Q：为什么我发的消息别人看不到前缀？**
A：本模组是客户端模组，前缀会作为消息文本的一部分发送到服务器。如果服务器有聊天插件改写了消息，前缀可能被覆盖。原版服务器和大部分服务器都能正常显示。

**Q：在服务器上会不会被认为是广告 / 刷屏？**
A：前缀是消息的一部分，相当于你自己打字带了前缀，是否违规取决于服务器规则，建议先在自己常玩的服务器测试。

**Q：能让所有玩家的聊天都加前缀吗？**
A：本模组是客户端模组，只影响你自己发出的消息。如果想让服务器上所有玩家的聊天都带前缀，需要把逻辑改成服务端实现，并安装到服务器上。

## 📄 许可证

[MIT](LICENSE)
