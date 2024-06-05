package xd.arkosammy.monkeyconfig.util

@JvmRecord
data class SettingIdentifier(val tableName: String, val settingName: String) {

    override fun toString(): String {
        return "$tableName.$settingName"
    }

}