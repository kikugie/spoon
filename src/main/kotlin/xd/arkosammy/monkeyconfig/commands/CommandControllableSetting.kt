package xd.arkosammy.monkeyconfig.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.util.Formatting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.managers.getTypedSetting
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.util.SettingLocation
import java.util.concurrent.CompletableFuture

/**
 * Represents a [ConfigSetting] that can be controlled via commands.
 * This is used to automatically create commands
 * to edit the values of the implementing [ConfigSetting]s for easier in-game interaction with the configuration file.
 */
interface CommandControllableSetting<out V : Any, T : ArgumentType<*>> {

    /**
     * The [ArgumentType] that will be used to create the [ArgumentCommandNode] for this setting.
     */
    val argumentType: T

    /**
     * An identifier used to associate the command for this [ConfigSetting] with a category in the command tree.
     */
    val commandIdentifier: SettingLocation

    /**
     * Returns a value that corresponds to the argument provided by the user whenever the corresponding [ArgumentCommandNode] node is invoked.
     *
     * @param [ctx] The command context provided by the [ArgumentCommandNode] callback.
     * @param [argumentName] The name of the argument as registered in the corresponding [ArgumentCommandNode].
     *
     * @return A value that will be used to update the value of the implementing [ConfigSetting].
     */
    fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String) : V

    /**
     * Returns a [CompletableFuture] of [Suggestions] that will be used in the [ArgumentCommandNode] of this [ConfigSetting]. By default, it calls [ArgumentType.listSuggestions].
     *
     * @param [ctx] The command context provided by the [ArgumentCommandNode] callback.
     * @param [suggestionsBuilder] A builder used to create [Suggestions] for an [ArgumentCommandNode]
     * @return a [CompletableFuture] of [Suggestions] used as part of this [ConfigSetting]'s command usage.
     */
    fun getSuggestions(ctx: CommandContext<ServerCommandSource>, suggestionsBuilder: SuggestionsBuilder) : CompletableFuture<Suggestions> =
        this.argumentType.listSuggestions(ctx, suggestionsBuilder)

    /**
     * The callback that will be run when the [ArgumentCommandNode]'s "executes" block is called.
     * By default, it simply retrieves the value and sets it to the [ConfigSetting] given by the [commandIdentifier].
     */
    val onValueSetCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int
        get() = get@{ ctx, configManager ->
            try {
                val setting: ConfigSetting<V, *> = configManager.getTypedSetting<V, ConfigSetting<V, *>>(this.commandIdentifier) ?: return@get Command.SINGLE_SUCCESS.also {
                    ctx.source.sendMessage(Text.literal("Config setting \"${this.commandIdentifier.settingName}\" was not found!").formatted(Formatting.RED))
                }
                val newValue: V = this.getArgumentValue(ctx, this.commandIdentifier.settingName)
                setting.value = newValue
                ctx.source.sendMessage(Text.literal("${this.commandIdentifier.settingName} has been set to : ${setting.value}"))
                return@get Command.SINGLE_SUCCESS
            } catch (e: Exception) {
                    ctx.source.sendMessage(Text.literal("Error attempting to set value for ${this.commandIdentifier.settingName}: ${e.message}"))
                return@get Command.SINGLE_SUCCESS
            }
        }

    /**
     * The callback that will be run when the [LiteralCommandNode]'s "execute" block is called.
     * By default, it simply retrieves the value of this [CommandControllableSetting] and shows it to the user.
     */
    val onValueGetCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int
        get() = get@{ ctx, configManager ->
            val setting: ConfigSetting<V, *> = configManager.getTypedSetting<V, ConfigSetting<V, *>>(this.commandIdentifier) ?: return@get Command.SINGLE_SUCCESS.also {
                ctx.source.sendMessage(Text.literal("Config setting \"${this.commandIdentifier.settingName}\" was not found!").formatted(Formatting.RED))
            }
            val currentValue: V = setting.value
            ctx.source.sendMessage(Text.literal("${this.commandIdentifier.settingName} currently set to: $currentValue"))
            return@get Command.SINGLE_SUCCESS
        }

    fun accept(visitor: CommandVisitor) {
        visitor.visit(this)
    }

}