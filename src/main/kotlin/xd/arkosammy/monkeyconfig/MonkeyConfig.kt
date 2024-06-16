package xd.arkosammy.monkeyconfig

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xd.arkosammy.monkeyconfig.registrars.DefaultConfigRegistrar

internal object MonkeyConfig : ModInitializer {

	const val MOD_ID: String = "monkeyconfig"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {

		ServerLifecycleEvents.SERVER_STOPPING.register {
			DefaultConfigRegistrar.saveAllManagers()
		}

		LOGGER.info("Thanks to isXander (https://github.com/isXander) for helping me get into Kotlin from Java :D")

	}
}