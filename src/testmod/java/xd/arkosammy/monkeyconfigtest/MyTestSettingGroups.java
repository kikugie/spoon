package xd.arkosammy.monkeyconfigtest;

import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup;
import xd.arkosammy.monkeyconfig.groups.maps.MutableStringMapSettingGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum MyTestSettingGroups {
    STRING_MAP_GROUP(new MutableStringMapSettingGroup("stringMap", null, Map.ofEntries(
            Map.entry("key1", "value1"),
            Map.entry("key2", "value2"),
            Map.entry("key3", "value3")
    )));

    private final MutableSettingGroup settingGroup;

    MyTestSettingGroups(MutableSettingGroup settingGroup) {
        this.settingGroup = settingGroup;
    }

    public static List<MutableSettingGroup> getSettingGroups() {
        return Arrays.stream(MyTestSettingGroups.values()).map(e -> e.settingGroup).toList();
    }

}
