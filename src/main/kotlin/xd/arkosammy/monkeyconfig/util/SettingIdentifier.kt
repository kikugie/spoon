package xd.arkosammy.monkeyconfig.util

/**
 * A data class used to associate a setting with a table by their names.
 */
@JvmRecord
data class SettingIdentifier(val tableName: String, val settingName: String) {

    override fun toString(): String {
        return "$tableName.$settingName"
    }

}