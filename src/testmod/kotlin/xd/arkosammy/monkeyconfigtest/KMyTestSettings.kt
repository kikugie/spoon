package xd.arkosammy.monkeyconfigtest

import xd.arkosammy.monkeyconfig.settings.ConfigSetting

enum class KMyTestSettings(private val settingBuilder: ConfigSetting.Builder<*, *, *>) {


    /*
    //BOOLEAN_LIST_SETTING(BooleanListSetting.Builder(SettingLocation("listTable", "myBooleanListSetting"), defaultValue = mutableListOf(true, false, true))),
    //NUMBER_LIST_SETTING(NumberListSetting.Builder(SettingLocation("listTable", "myIntListSetting"), defaultValue = mutableListOf(2, 4, 6, 8, 10))),
    //STRING_LIST_SETTING(StringListSetting.Builder(SettingLocation("listTable", "myStringListSetting"), defaultValue = mutableListOf("Hello", "my", "name", "is", "Arkosammy")));

    companion object {

        fun getSettingBuilders() : List<ConfigSetting.Builder<*, *, *>> {
            return KMyTestSettings.entries.map { testSetting -> testSetting.settingBuilder }
        }

    }

     */

}