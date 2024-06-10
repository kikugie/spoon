package xd.arkosammy.monkeyconfig.commands.visitors

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * A visitor that is used to visit [CommandControllableSetting]s and create command nodes for them in the command tree.
 * It consumes a [ConfigManager] that will be used for the command callbacks and to reload the config file,
 * and a [CommandDispatcher] to register the command nodes with.
 *
 */
interface CommandVisitor {

    val configNode: LiteralCommandNode<ServerCommandSource>

    val onConfigReloadedCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int

    fun <V : Any, T : ArgumentType<*>> visit(commandControllableSetting: CommandControllableSetting<V, T>)

}