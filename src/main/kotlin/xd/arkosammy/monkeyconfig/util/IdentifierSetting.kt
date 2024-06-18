package xd.arkosammy.monkeyconfig.util

import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Identifier
import xd.arkosammy.monkeyconfig.settings.AbstractCommandControllableSetting
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.StringType

open class IdentifierSetting @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    override val defaultValue: Identifier,
    override var value: Identifier = defaultValue) : AbstractCommandControllableSetting<Identifier, StringType, IdentifierArgumentType>(settingLocation, comment, defaultValue, value) {

    override val valueToSerializedConverter: (Identifier) -> StringType
        get() = { identifier -> StringType(identifier.toString()) }

    override val serializedToValueConverter: (StringType) -> Identifier
        get() = { stringType -> Identifier(stringType.rawValue) }

    override val argumentType: IdentifierArgumentType
        get() = IdentifierArgumentType.identifier()

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): Identifier =
        IdentifierArgumentType.getIdentifier(ctx, argumentName)

    class  Builder @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: Identifier) : ConfigSetting.Builder<IdentifierSetting, Identifier, StringType>(settingLocation, comment, defaultValue) {

        override fun build(): IdentifierSetting = IdentifierSetting(settingLocation, comment, defaultValue)

    }

}