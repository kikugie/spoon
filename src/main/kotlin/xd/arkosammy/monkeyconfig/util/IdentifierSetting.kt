package xd.arkosammy.monkeyconfig.util

import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Identifier
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.StringType

open class IdentifierSetting @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    override val defaultValue: Identifier,
    override var value: Identifier = defaultValue) : ConfigSetting<Identifier, StringType>(settingLocation, comment, defaultValue, value), CommandControllableSetting<Identifier, IdentifierArgumentType> {

    override val serializedValue: StringType
        get() = StringType(this.value.toString())

    override val serializedDefaultValue: StringType
        get() = StringType(this.defaultValue.toString())

    override fun setValueFromSerialized(serializedValue: StringType) {
        this.value = Identifier(serializedValue.value)
    }

    override val argumentType: IdentifierArgumentType
        get() = IdentifierArgumentType.identifier()

    override val commandIdentifier: SettingLocation
        get() = this.settingLocation

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): Identifier {
        return IdentifierArgumentType.getIdentifier(ctx, argumentName)
    }

}