package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

abstract class ConfigSetting<T : Any> @JvmOverloads constructor(open val settingIdentifier: SettingIdentifier, open val comment: String? = null, open val defaultValue: T, open var value: T = defaultValue) {

    fun resetValue() {
        this.value = this.defaultValue
    }

    abstract class Builder<V : Any, S : ConfigSetting<V>>(protected val id: SettingIdentifier, protected val defaultValue: V, protected var comment: String? = null) {

        fun withComment(comment: String) : Builder<V, S> {
            this.comment = comment
            return this
        }

        val tableName: String
            get() = id.tableName

        abstract fun build() : S

    }

}