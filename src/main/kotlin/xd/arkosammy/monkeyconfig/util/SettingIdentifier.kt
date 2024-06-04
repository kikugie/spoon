package xd.arkosammy.monkeyconfig.util

data class SettingIdentifier(val tableName: String, val settingName: String) {

    override fun toString(): String {
        return "$tableName.$settingName"
    }

}