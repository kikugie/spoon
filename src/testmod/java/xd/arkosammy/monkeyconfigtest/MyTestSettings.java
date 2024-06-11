package xd.arkosammy.monkeyconfigtest;

import xd.arkosammy.monkeyconfig.settings.CommandControllableEnumSetting;
import xd.arkosammy.monkeyconfig.settings.ConfigSetting;
import xd.arkosammy.monkeyconfig.settings.EnumSetting;
import xd.arkosammy.monkeyconfig.settings.NumberSetting;
import xd.arkosammy.monkeyconfig.util.SettingIdentifier;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MyTestSettings {
    MY_NUM_SETTING(new NumberSetting.Builder<>(new SettingIdentifier("testTable1", "myNumSetting"), 1)),
    MY_ENUM_SETTING(new CommandControllableEnumSetting.Builder<>(new SettingIdentifier("testTable1", "myEnumSetting"), EnumClass.HAPPY));

    private final ConfigSetting.Builder<?, ?, ?> builder;

    MyTestSettings(ConfigSetting.Builder<?, ?, ?> builder){

        this.builder = builder;

    }

    public static List<ConfigSetting.Builder<?, ?, ?>> getBuilders() {
        return Arrays.stream(MyTestSettings.values()).map(setting -> setting.builder).collect(Collectors.toUnmodifiableList());
    }

}
