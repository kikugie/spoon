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

class DefaultCommandVisitor(configManager: ConfigManager, commandDispatcher: CommandDispatcher<ServerCommandSource>, rootNodeName: String = configManager.configName, commandRegistryAccess: CommandRegistryAccess? = null, registrationEnvironment: CommandManager.RegistrationEnvironment? = null) : CommandVisitor(configManager, commandDispatcher, rootNodeName, commandRegistryAccess, registrationEnvironment) {

    private val configCategories: List<String> = this.configManager.configTables.map { table ->  table.name }.toList()
    private val cachedCategoryNodes: MutableList<LiteralCommandNode<ServerCommandSource>> = mutableListOf()

    override fun <V : Any, T : ArgumentType<V>> visit(commandControllableSetting: CommandControllableSetting<V, T>) {

        val settingName: String = commandControllableSetting.commandIdentifier.settingName
        val settingCategory: String = commandControllableSetting.commandIdentifier.tableName

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

        val setNode: ArgumentCommandNode<ServerCommandSource, V> = CommandManager
            .argument(settingName, commandControllableSetting.argumentType)
            .requires { source -> source.hasPermissionLevel(4) }
            .executes { ctx -> commandControllableSetting.onValueSetCallback(ctx, this.configManager) }
            .build()

        for(node: LiteralCommandNode<ServerCommandSource> in this.cachedCategoryNodes) {
            if(node.literal != settingCategory) {
                continue
            }
            node.addChild(settingNode)
            node.addChild(setNode)
        }

    }

}