package xd.arkosammy.monkeyconfig.util

import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Identifier
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class IdentifierSetting @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    override val defaultValue: Identifier,
    override var value: Identifier = defaultValue) : ConfigSetting<Identifier, StringType>(settingIdentifier, comment, defaultValue, value), CommandControllableSetting<Identifier, IdentifierArgumentType> {

    override val serializedValue: StringType
        get() = StringType(this.value.toString())

    override val serializedDefaultValue: StringType
        get() = StringType(this.defaultValue.toString())

    override fun setValueFromSerialized(serializedValue: StringType) {
        this.value = Identifier(serializedValue.value)
    }

    override val argumentType: IdentifierArgumentType
        get() = IdentifierArgumentType.identifier()

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): Identifier {
        return IdentifierArgumentType.getIdentifier(ctx, argumentName)
    }

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this)
    }

}