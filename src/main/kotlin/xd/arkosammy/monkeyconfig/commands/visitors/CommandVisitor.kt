package xd.arkosammy.monkeyconfig.commands.visitors

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * A visitor
 * that is used to consume [CommandControllableSetting]s and create command nodes for them in the command tree.
 * These [CommandControllableSetting]s are usually provided by a [ConfigManager],
 * and the player can update the values of the configuration file
 * managed by the [ConfigManager] using the commands registered by this [CommandVisitor].
 */
interface CommandVisitor {

    /**
     * The root [LiteralCommandNode] that contains all other setting command categories.
     */
    val configNode: LiteralCommandNode<ServerCommandSource>

    /**
     * A callback
     * that can be used
     * to run code whenever the [ConfigManager] used by this [CommandVisitor] is reloaded using a command.
     */
    val onConfigReloadedCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int

    /**
     * Consumes a [commandControllableSetting] and implements the logic for registering a command to modify it.
     */
    fun <V : Any, T : ArgumentType<*>> visit(commandControllableSetting: CommandControllableSetting<V, T>)

}