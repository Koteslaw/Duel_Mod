package net.koteslaw

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry
import net.koteslaw.commands.KitArgumentType
import net.koteslaw.commands.KitCommand
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import java.util.function.Supplier

object Duel : ModInitializer {
    private val logger = LoggerFactory.getLogger("duel")

	override fun onInitialize() {
		KitCommand.register()
		ArgumentTypeRegistry.registerArgumentType(Identifier.of("duel:kit_argument"),KitArgumentType::class.java, ConstantArgumentSerializer.of(Supplier { KitArgumentType.create() }))



		logger.info("Hello Fabric world!")
	}
}