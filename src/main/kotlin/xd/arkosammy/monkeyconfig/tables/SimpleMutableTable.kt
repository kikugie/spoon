package xd.arkosammy.monkeyconfig.tables

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.SerializableType

open class SimpleMutableTable @JvmOverloads constructor(
    override val configSettings: MutableList<ConfigSetting<*, *>>,
    name: String, comment: String? = null,
    loadBeforeSave: Boolean = false,
    registerSettingsAsCommands: Boolean = true) : SimpleTable(configSettings, name, comment, loadBeforeSave, registerSettingsAsCommands), MutableConfigTable {

    override fun <T, S : SerializableType<*>> addConfigSetting(setting: ConfigSetting<T, S>) {
        this.configSettings.add(setting)
    }

    override fun toImmutable(): ConfigTable {
        return SimpleTable(configSettings.toList(), name, comment, loadBeforeSave)
    }
}