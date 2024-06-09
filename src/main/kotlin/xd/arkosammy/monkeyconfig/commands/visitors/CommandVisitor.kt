package xd.arkosammy.monkeyconfig.commands.visitors

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * A visitor that is used to visit [CommandControllableSetting]s and create command nodes for them in the command tree.
 * It consumes a [ConfigManager] that will be used for the command callbacks and to reload the config file,
 * and a [CommandDispatcher] to register the command nodes with.
 *
 * Implementors should be aware that the constructor of this class already registers the root node, the config node, as well as the relaod node, so there is no need to register them again
 */
abstract class CommandVisitor(protected val configManager: ConfigManager, protected val commandDispatcher: CommandDispatcher<ServerCommandSource>, rootNodeName: String = configManager.configName, protected val commandRegistryAccess: CommandRegistryAccess? = null, protected val registrationEnvironment: CommandManager.RegistrationEnvironment? = null) {

    protected val configNode: LiteralCommandNode<ServerCommandSource> = CommandManager
        .literal("config")
        .requires { source -> source.hasPermissionLevel(4) }
        .build()

    protected val onConfigReloadedCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int
        get() = get@{ ctx, configManager ->
            if(configManager.reloadFromFile()) {
                ctx.source.sendMessage(Text.literal("Config reloaded successfully!").formatted(Formatting.GREEN))
            } else {
                ctx.source.sendMessage(Text.literal("Found no existing config file to reload from.").formatted(Formatting.RED))
            }
            return@get Command.SINGLE_SUCCESS
        }

    init {

        val rootNode: LiteralCommandNode<ServerCommandSource> = CommandManager
            .literal(rootNodeName)
            .requires { source -> source.hasPermissionLevel(4) }
            .build()

        val reloadNode: LiteralCommandNode<ServerCommandSource> = CommandManager
            .literal("reload")
            .requires { source -> source.hasPermissionLevel(4) }
            .executes { ctx -> onConfigReloadedCallback(ctx, configManager) }
            .build()

        commandDispatcher.root.addChild(rootNode)
        rootNode.addChild(configNode)
        configNode.addChild(reloadNode)
    }

    abstract fun <V : Any, T : ArgumentType<*>> visit(commandControllableSetting: CommandControllableSetting<V, T>)

}