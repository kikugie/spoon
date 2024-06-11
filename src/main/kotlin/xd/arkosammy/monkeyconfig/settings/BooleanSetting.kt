package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class BooleanSetting @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: Boolean,
    value: Boolean = defaultValue) : ConfigSetting<Boolean, BooleanType>(settingIdentifier, comment, defaultValue, value), CommandControllableSetting<Boolean, BoolArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): Boolean {
        return BoolArgumentType.getBool(ctx, argumentName)
    }

    override val serializedValue: BooleanType
        get() = BooleanType(this.value)

    override val serializedDefaultValue: BooleanType
        get() = BooleanType(this.defaultValue)

    override fun setValueFromSerialized(serializedValue: BooleanType) {
        this.value = serializedValue.value
    }

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this)
    }

    override val argumentType : BoolArgumentType
        get() = BoolArgumentType.bool()

    class Builder @JvmOverloads constructor(id: SettingIdentifier, comment: String? = null, defaultValue: Boolean) : ConfigSetting.Builder<Boolean, BooleanType, BooleanSetting>(id, comment, defaultValue) {

        override fun build(): BooleanSetting {
            return BooleanSetting(this.id, this.comment, this.defaultValue)
        }

    }
}