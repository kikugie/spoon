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
import xd.arkosammy.monkeyconfig.groups.SettingGroup


/**
 * Default implementation of [CommandVisitor] that creates a [LiteralCommandNode] for editing configuration settings,
 * as well as a node for a "reload" command to reload the [ConfigManager] that this [CommandVisitor] accepts.
 * It overrides [onConfigReloadedCallback] and it simply attempts to reload the [ConfigManager] of this class,
 * and shows whether this operation was successful, to the user.
 *
 * @param [configManager] The [ConfigManager] from which to retrieve [CommandControllableSetting]s from. The name of the [SettingGroup] associated with the setting used as the command category of the respective command.
 * @param [rootNodeName] The name of the [LiteralCommandNode] that will contain all other configuration related commands.
 * @param [commandDispatcher] The [CommandDispatcher] to use to register commands to.
 * @param [commandRegistryAccess] An optional [CommandRegistryAccess] instance to use with commands.
 * @param [registrationEnvironment] An optional [CommandManager.RegistrationEnvironment] instance to ues with commands.
 */
abstract class AbstractCommandVisitor @JvmOverloads constructor(
    protected val configManager: ConfigManager,
    rootNodeName: String = configManager.configName,
    protected val commandDispatcher: CommandDispatcher<ServerCommandSource>,
    protected val commandRegistryAccess: CommandRegistryAccess? = null,
    protected val registrationEnvironment: CommandManager.RegistrationEnvironment? = null) : CommandVisitor {

    final override val configNode: LiteralCommandNode<ServerCommandSource> = CommandManager
        .literal("config")
        .requires { source -> source.hasPermissionLevel(4) }
        .build()

    override val onConfigReloadedCallback: (CommandContext<ServerCommandSource>, ConfigManager) -> Int
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

    abstract override fun <V : Any, T : ArgumentType<*>> visit(commandControllableSetting: CommandControllableSetting<V, T>)

}