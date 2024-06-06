package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.BoolArgumentType
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class BooleanSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: Boolean, value: Boolean = defaultValue) : ConfigSetting<Boolean>(settingIdentifier, comment, defaultValue, value), CommandControllableSetting<Boolean, BoolArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this)
    }

    override val argumentClass: Class<Boolean>
        get() = Boolean::class.java

    override val argumentType : BoolArgumentType
        get() = BoolArgumentType.bool()

    class Builder(id: SettingIdentifier, defaultValue: Boolean) : ConfigSetting.Builder<Boolean, BooleanSetting>(id, defaultValue) {

        override fun build(): BooleanSetting {
            return BooleanSetting(this.id, this.comment, this.defaultValue)
        }

    }
}