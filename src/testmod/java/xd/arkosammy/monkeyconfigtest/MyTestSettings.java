package xd.arkosammy.monkeyconfigtest;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import xd.arkosammy.monkeyconfig.settings.CommandControllableEnumSetting;
import xd.arkosammy.monkeyconfig.settings.ConfigSetting;
import xd.arkosammy.monkeyconfig.settings.NumberSetting;
import xd.arkosammy.monkeyconfig.settings.list.BooleanListSetting;
import xd.arkosammy.monkeyconfig.settings.list.NumberListSetting;
import xd.arkosammy.monkeyconfig.settings.list.StringListSetting;
import xd.arkosammy.monkeyconfig.util.BlockPosSetting;
import xd.arkosammy.monkeyconfig.util.IdentifierSetting;
import xd.arkosammy.monkeyconfig.util.SettingLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum MyTestSettings {
    // Enum setting
    MY_ENUM_SETTING(new CommandControllableEnumSetting.Builder<>(new SettingLocation("enumSettingTable", "myEnumSetting"), EnumClass.HAPPY)),
    // Numerical settings
    MY_BYTE_SETTING(new NumberSetting.Builder<>(new SettingLocation("numTable", "myByteSetting"), (byte) 1)),
    MY_SHORT_SETTING(new NumberSetting.Builder<>(new SettingLocation("numTable", "myShortSetting"),  (short) 1)),
    MY_INT_SETTING(new NumberSetting.Builder<>(new SettingLocation("numTable", "myIntSetting"), 1)),
    MY_LONG_SETTING(new NumberSetting.Builder<>(new SettingLocation("numTable", "myLongSetting"), 1L)),
    MY_FLOAT_SETTING(new NumberSetting.Builder<>(new SettingLocation("numTable", "myFloatSetting"), 1.0f)),
    MY_DOUBLE_SETTING(new NumberSetting.Builder<>(new SettingLocation("numTable", "myDoubleSetting"), 1.0d)),

    // Special settings
    BLOCK_POS_SETTING(new BlockPosSetting.Builder(new SettingLocation("specialTable", "myBlockPosSetting"), new BlockPos(1, 2, 3))),
    IDENTIFIER_SETTING(new IdentifierSetting.Builder(new SettingLocation("specialTable", "myIdentifierSetting"), Objects.requireNonNull(Identifier.of("monkey", "test")))),

    // List settings
    BOOLEAN_LIST_SETTING(new BooleanListSetting.Builder(new SettingLocation("listTable", "myBooleanListSetting"), null, Arrays.asList(true, false, false, true))),
    NUMBER_LIST_SETTING(new NumberListSetting.Builder<>(new SettingLocation("listTable", "myIntListSetting"), null, Arrays.asList(2, 4, 6, 8, 10))),
    STRING_LIST_SETTING(new StringListSetting.Builder(new SettingLocation("listTable", "myStringListSetting"), null, Arrays.asList("hello", "my", "name", "is", "arkosammy12")));

    private final ConfigSetting.Builder<?, ?, ?> builder;

    MyTestSettings(ConfigSetting.Builder<?, ?, ?> builder){
        this.builder = builder;
    }

    public static List<ConfigSetting.Builder<?, ?, ?>> getBuilders() {
        return Arrays.stream(MyTestSettings.values()).map(setting -> setting.builder).collect(Collectors.toUnmodifiableList());
    }

}
