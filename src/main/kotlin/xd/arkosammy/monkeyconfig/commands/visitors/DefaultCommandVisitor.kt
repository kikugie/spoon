package xd.arkosammy.monkeyconfig.commands.visitors

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting

class DefaultCommandVisitor<V : Any, T : ArgumentType<V>>(commandDispatcher: CommandDispatcher<ServerCommandSource>, commandRegistryAccess: CommandRegistryAccess, registrationEnvironment: CommandManager.RegistrationEnvironment) : CommandVisitor<V, T>(commandDispatcher, commandRegistryAccess, registrationEnvironment) {

    override fun visit(commandControllableSetting: CommandControllableSetting<V, T>) {
        TODO("Not yet implemented")
    }

}