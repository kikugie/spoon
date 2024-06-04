package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

// TOOD: Fix subclass constructors to include defaultValue and starting value
abstract class ConfigSetting<T>(open val name: String, open val comment: String? = null, open val defaultValue: T, open var value: T = defaultValue) {

    fun resetValue() {
        this.value = this.defaultValue
    }

    abstract class Builder<V, S : ConfigSetting<V>>(protected val id: SettingIdentifier, protected val defaultValue: V, protected var comment: String? = null) {

        fun withComment(comment: String) : Builder<V, S> {
            this.comment = comment
            return this
        }

        val tableName: String
            get() = id.tableName

        abstract fun build() : S

    }

}