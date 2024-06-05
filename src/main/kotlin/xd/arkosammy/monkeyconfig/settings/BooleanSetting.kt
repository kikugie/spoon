package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.BoolArgumentType
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class BooleanSetting(configManager: ConfigManager, settingIdentifier: SettingIdentifier, comment: String?, value: Boolean) : ConfigSetting<Boolean>(configManager, settingIdentifier, comment, value), CommandControllableSetting<Boolean, BoolArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun accept(visitor: CommandVisitor<Boolean, BoolArgumentType>) {
        visitor.visit(this)
    }

    override val argumentClass: Class<Boolean>
        get() = Boolean::class.java

    override val argumentType : BoolArgumentType
        get() = BoolArgumentType.bool()

    class Builder(id: SettingIdentifier, defaultValue: Boolean) : ConfigSetting.Builder<Boolean, BooleanSetting>(id, defaultValue) {

        override fun build(configManager: ConfigManager): BooleanSetting {
            return BooleanSetting(configManager, this.id, this.comment, this.defaultValue)
        }

    }
}