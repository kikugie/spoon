package xd.arkosammy.monkeyconfig.commands.visitors

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting

abstract class CommandVisitor<V : Any, T : ArgumentType<V>>(protected val commandDispatcher: CommandDispatcher<ServerCommandSource>, protected val commandRegistryAccess: CommandRegistryAccess, protected val registrationEnvironment: CommandManager.RegistrationEnvironment) {
    // TODO: Implement the setup for config commands, which includes the root config node. Think about whether this should consume a config manager to provide the settings

    abstract fun visit(commandControllableSetting: CommandControllableSetting<V, T>)

}