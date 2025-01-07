package net.koteslaw

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.koteslaw.commands.KitCommand
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandManager.RegistrationEnvironment
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

object Duel : ModInitializer {
    private val logger = LoggerFactory.getLogger("duel")

	override fun onInitialize() {
		KitCommand.register()




		logger.info("Hello Fabric world!")
	}
}