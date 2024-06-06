package xd.arkosammy.monkeyconfig.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import com.mojang.brigadier.tree.ArgumentCommandNode
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.managers.getTypedSetting
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

/**
 * Represents an entity that can be controlled via commands. This is used to create command nodes for settings in the command tree.
 */
interface CommandControllableSetting<V : Any, T : ArgumentType<V>> {

    /**
     * The class of the argument type that will be used to retrieve the argument value from the command context.
     */
    val argumentClass: Class<V>

    /**
     * The argument that will be used to create the [ArgumentCommandNode] for this setting.
     */
    val argumentType: T

    /**
     * An identifier used to associate the command for this setting with a category in the command tree.
     */
    val commandIdentifier: SettingIdentifier

    /**
     * The callback tha will be executed whenever the value of this setting is set via command. By default, it simply retrieves the value and sets it to the setting corresponding to the [commandIdentifier].
     */
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

    /**
     * The callback that will be executed whenever the value of this setting is retrieved via command. By default, it simply retrieves the value using the [commandIdentifier] and sends it to the command source as a message.
     */
    val onValueGetCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int
        get() = get@{ ctx, configManager ->
            val setting: ConfigSetting<V> = configManager.getTypedSetting(this.commandIdentifier)
            val currentValue: V = setting.value
            ctx.source.sendMessage(Text.literal("${this.commandIdentifier.settingName} currently set to: $currentValue"))
            return@get Command.SINGLE_SUCCESS
        }

    fun accept(visitor: CommandVisitor)

}