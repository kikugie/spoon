package xd.arkosammy.monkeyconfig.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.managers.getTypedSetting
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

interface CommandControllableSetting<V : Any, T : ArgumentType<V>> {

    val argumentClass: Class<V>

    val argumentType: T

    val commandIdentifier: SettingIdentifier

    val onValueSetCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int
        get() = get@{ ctx, configManager ->
            try {
                val newValue: V = ctx.getArgument(this.commandIdentifier.settingName, this.argumentClass)
                val setting: ConfigSetting<V> = configManager.getTypedSetting(this.commandIdentifier)
                setting.value =  newValue
                return@get Command.SINGLE_SUCCESS
            } catch (e: IllegalArgumentException) {
                    ctx.source.sendMessage(Text.literal("Error attempting to set value for ${this.commandIdentifier.settingName}: ${e.message}"))
                return@get Command.SINGLE_SUCCESS
            }
        }

    val onValueGetCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int
        get() = get@{ ctx, configManager ->
            val setting: ConfigSetting<V> = configManager.getTypedSetting(this.commandIdentifier)
            val currentValue: V = setting.value
            ctx.source.sendMessage(Text.literal("${this.commandIdentifier.settingName} currently set to: $currentValue"))
            return@get Command.SINGLE_SUCCESS
        }

    fun accept(visitor: CommandVisitor<V, T>)

}