package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import com.electronwill.nightconfig.yaml.YamlFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.tables.MutableConfigTable
import java.nio.file.Path

open class YamlConfigManager : AbstractConfigManager {

    constructor(configName: String, configTables: List<ConfigTable>) : super(configName, configTables)

    constructor(configName: String, configTables: List<MutableConfigTable>, settingBuilders: List<ConfigSetting.Builder<*, *>>) : super(configName, configTables, settingBuilders)

    final override val configPath: Path = FabricLoader.getInstance().configDir.resolve("$configName.yaml")
    override val configBuilder: GenericBuilder<out Config, out FileConfig> = FileConfig.builder(this.configPath, YamlFormat.defaultInstance())
        .preserveInsertionOrder()
        .autoreload()
        .sync()
        .onSave { this.onSave }
        .onLoad { this.onLoaded }
        .onAutoReload { this.onAutoReload }

}