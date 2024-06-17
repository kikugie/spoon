package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class StringSetting @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: String,
    value: String = defaultValue) : ConfigSetting<String, StringType>(settingLocation, comment, value), CommandControllableSetting<String, StringArgumentType> {

    override val valueToSerializedConverter: (String) -> StringType
        get() = { string -> StringType(string) }

    override val serializedToValueConverter: (StringType) -> String
        get() = { stringType -> stringType.rawValue }

    override val commandIdentifier: SettingLocation
        get() = this.settingLocation

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): String {
        return StringArgumentType.getString(ctx, argumentName)
    }

    override val argumentType: StringArgumentType
        get() = StringArgumentType.string()

    open class Builder @JvmOverloads constructor(id: SettingLocation, comment: String? = null, defaultValue: String) : ConfigSetting.Builder<StringSetting, String, StringType>(id, comment, defaultValue) {

        override fun build(): StringSetting {
            return StringSetting(this.settingLocation, this.comment, this.defaultValue)
        }

    }

}