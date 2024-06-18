package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class BooleanSetting @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: Boolean,
    value: Boolean = defaultValue) : AbstractCommandControllableSetting<Boolean, BooleanType, BoolArgumentType>(settingLocation, comment, defaultValue, value) {

    override val valueToSerializedConverter: (Boolean) -> BooleanType
        get() = { boolean -> BooleanType(boolean) }

    override val serializedToValueConverter: (BooleanType) -> Boolean
        get() = { booleanType -> booleanType.rawValue }

    override val argumentType : BoolArgumentType
        get() = BoolArgumentType.bool()

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): Boolean =
        BoolArgumentType.getBool(ctx, argumentName)


    open class Builder @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: Boolean) : ConfigSetting.Builder<BooleanSetting, Boolean, BooleanType>(settingLocation, comment, defaultValue) {

        override fun build(): BooleanSetting = BooleanSetting(this.settingLocation, this.comment, this.defaultValue)

    }
}