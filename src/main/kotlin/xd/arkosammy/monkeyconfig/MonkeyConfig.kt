package xd.arkosammy.monkeyconfig

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.command.CommandManager
import net.minecraft.util.math.BlockPos
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.commands.visitors.DefaultCommandVisitor
import xd.arkosammy.monkeyconfig.managers.*
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.settings.NumberSetting
import xd.arkosammy.monkeyconfig.tables.SimpleConfigTable
import xd.arkosammy.monkeyconfig.settings.util.BlockPosSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

// TODO: Continue properly documenting
object MonkeyConfig : ModInitializer {

	const val MOD_ID: String = "monkeyconfig"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	//val manager: ConfigManager = TomlConfigManager("test", listOf(SimpleConfigTable(name = "testTable", comment = "tableComment", configSettings = listOf(
	//	BlockPosSetting(settingIdentifier = SettingIdentifier("testTable", "blockPosSetting"), defaultValue = BlockPos(1, 2, 3))
	//))))

	override fun onInitialize() {

		//val numberSetting: NumberSetting<Byte> = NumberSetting(SettingIdentifier("foo", "bar"), "blah", 1)
		//println("Argument type: ${numberSetting.argumentType}")
		/*
		CommandRegistrationCallback.EVENT.register { commandDispatcher, commandRegistryAccess, registrationEnvironment ->
			val commandVisitor: CommandVisitor = DefaultCommandVisitor(manager, commandDispatcher)
			for(table: ConfigTable in manager.configTables) {
				for(setting: ConfigSetting<*, *> in table.configSettings) {
					if(setting !is CommandControllableSetting<*, *>) {
						continue
					}
					setting.accept(commandVisitor)
				}
			}

		}

		ServerLifecycleEvents.SERVER_STOPPING.register {
			manager.saveToFile()
			println("we finished saving bois")
		}

		 */

		LOGGER.info("Thanks to isXander (https://github.com/isXander) for helping me get into Kotlin from Java :D")

	}
}