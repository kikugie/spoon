package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class BooleanSetting @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: Boolean,
    value: Boolean = defaultValue) : ConfigSetting<Boolean, BooleanType>(settingLocation, comment, defaultValue, value), CommandControllableSetting<Boolean, BoolArgumentType> {

    override val serializedValue: BooleanType
        get() = BooleanType(this.value)

    override val serializedDefaultValue: BooleanType
        get() = BooleanType(this.defaultValue)

    override fun setValueFromSerialized(serializedValue: BooleanType) {
        this.value = serializedValue.value
    }

    override val commandIdentifier: SettingLocation
        get() = this.settingLocation

    override val argumentType : BoolArgumentType
        get() = BoolArgumentType.bool()

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): Boolean {
        return BoolArgumentType.getBool(ctx, argumentName)
    }

    class Builder @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: Boolean) : ConfigSetting.Builder<BooleanSetting, Boolean, BooleanType>(settingLocation, comment, defaultValue) {

        override fun build(): BooleanSetting {
            return BooleanSetting(this.settingLocation, this.comment, this.defaultValue)
        }

    }
}