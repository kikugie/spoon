package xd.arkosammy.monkeyconfig.commands.visitors

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * The default implementation of [AbstractCommandVisitor].
 * It uses an internal [List] of Strings
 * to determine the command categories to use for the consumed [CommandControllableSetting]s,
 * and a [List] of [LiteralCommandNode] to keep track of already registered command node categories.
 */
class DefaultCommandVisitor @JvmOverloads constructor(
    configManager: ConfigManager,
    rootNodeName: String = configManager.configName,
    commandDispatcher: CommandDispatcher<ServerCommandSource>,
    commandRegistryAccess: CommandRegistryAccess? = null,
    registrationEnvironment: CommandManager.RegistrationEnvironment? = null) : AbstractCommandVisitor(configManager, rootNodeName, commandDispatcher, commandRegistryAccess, registrationEnvironment) {

    private val configCategories: List<String> = this.configManager.settingGroups.map { table ->  table.name }.toList()
    private val cachedCategoryNodes: MutableList<LiteralCommandNode<ServerCommandSource>> = mutableListOf()

    override fun <V : Any, T : ArgumentType<*>> visit(commandControllableSetting: CommandControllableSetting<V, T>) {

        val settingName: String = commandControllableSetting.commandIdentifier.settingName
        val settingCategory: String = commandControllableSetting.commandIdentifier.groupName

        // Return if the setting category of the setting is not in the list of config categories to register commands with
        if (!this.configCategories.contains(settingCategory)) {
            return
        }

        // If the setting category is not already registered as a command node, register it
        if(!this.cachedCategoryNodes.any { node -> node.literal == settingCategory }) {
            val categoryNode: LiteralCommandNode<ServerCommandSource> = CommandManager
                .literal(settingCategory)
                .requires { source -> source.hasPermissionLevel(4) }
                .build()

            this.cachedCategoryNodes.add(categoryNode)
            this.configNode.addChild(categoryNode)
        }

        val settingNode: LiteralCommandNode<ServerCommandSource> = CommandManager
            .literal(settingName)
            .requires { source -> source.hasPermissionLevel(4) }
            .executes { ctx -> commandControllableSetting.onValueGetCallback(ctx, this.configManager) }
            .build()

        val setterNode: ArgumentCommandNode<ServerCommandSource, out Any> = CommandManager
            .argument(settingName, commandControllableSetting.argumentType)
            .requires { source -> source.hasPermissionLevel(4) }
            .suggests { ctx, suggestionsBuilder -> commandControllableSetting.getSuggestions(ctx, suggestionsBuilder) }
            .executes { ctx -> commandControllableSetting.onValueSetCallback(ctx, this.configManager) }
            .build()

        for(categoryNode: LiteralCommandNode<ServerCommandSource> in this.cachedCategoryNodes) {
            if(categoryNode.literal != settingCategory) {
                continue
            }
            categoryNode.addChild(settingNode)
            settingNode.addChild(setterNode)
        }

    }

}