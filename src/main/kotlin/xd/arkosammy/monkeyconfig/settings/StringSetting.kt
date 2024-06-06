package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.StringArgumentType
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class StringSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: String, value: String = defaultValue) : ConfigSetting<String>(settingIdentifier, comment, value), CommandControllableSetting<String, StringArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this)
    }

    override val argumentClass: Class<String>
        get() = String::class.java

    override val argumentType: StringArgumentType
        get() = StringArgumentType.string()

    class Builder(id: SettingIdentifier, defaultValue: String) : ConfigSetting.Builder<String, StringSetting>(id, defaultValue) {

        override fun build(): StringSetting {
            return StringSetting(this.id, this.comment, this.defaultValue)
        }

    }
}