package xd.arkosammy.monkeyconfig

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// TODO: Continue properly documenting
object MonkeyConfig : ModInitializer {

	const val MOD_ID: String = "monkeyconfig"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	//val manager: ConfigManager = TODO()

	override fun onInitialize() {

		/*
		CommandRegistrationCallback.EVENT.register { commandDispatcher, commandRegistryAccess, registrationEnvironment ->

			val commandVisitor: CommandVisitor = DefaultCommandVisitor(manager, commandDispatcher)
			for(table: ConfigTable in manager.configTables) {
				for(setting: ConfigSetting<*> in table.configSettings) {
					if(setting !is CommandControllableSetting<*, *>) {
						continue
					}
					setting.accept(commandVisitor)
				}
			}

		}

		 */

	}
}