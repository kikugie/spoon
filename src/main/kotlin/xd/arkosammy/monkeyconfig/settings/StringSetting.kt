package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.StringArgumentType
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class StringSetting(configManager: ConfigManager, settingIdentifier: SettingIdentifier, comment: String?, value: String) : ConfigSetting<String>(configManager, settingIdentifier, comment, value), CommandControllableSetting<String, StringArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun accept(visitor: CommandVisitor<String, StringArgumentType>) {
        visitor.visit(this)
    }

    override val argumentClass: Class<String>
        get() = String::class.java

    override val argumentType: StringArgumentType
        get() = StringArgumentType.string()

    class Builder(id: SettingIdentifier, defaultValue: String) : ConfigSetting.Builder<String, StringSetting>(id, defaultValue) {

        override fun build(configManager: ConfigManager): StringSetting {
            return StringSetting(configManager, this.id, this.comment, this.defaultValue)
        }

    }
}