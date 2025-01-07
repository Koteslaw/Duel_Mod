package net.koteslaw.commands



import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.server.command.CommandManager.RegistrationEnvironment
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text


class KitCommand {
    companion object{
        fun register(){
            CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource?>, registryAccess: CommandRegistryAccess?, environment: RegistrationEnvironment? ->
                dispatcher.register(
                    literal("kit")
                        .then(
                            literal("save")
                                .executes { context: CommandContext<ServerCommandSource> ->
                                    val serverState: SaveData = SaveData.getServerState(context.source.server)
                                    val player = context.source.player
                                    if(player!=null){
                                        var inv = serverState.kits["test"]
                                        if(inv==null){
                                            inv = PlayerInventory(null)
                                        }
                                        inv.clone(player.inventory)
                                        serverState.kits["test"] =  inv
                                        context.source.sendFeedback({ Text.literal("saved") }, false)
                                    }
                                    1
                                }
                        )
                        .then(
                            literal("load")
                                .executes{ context: CommandContext<ServerCommandSource> ->
                                    val serverState: SaveData = SaveData.getServerState(context.source.server)
                                    val player = context.source.player
                                    val inventory = serverState.kits["test"]
                                    if(inventory!=null){
                                        player!!.inventory.clone(inventory)
                                        context.source.sendFeedback({ Text.literal("loaded") }, false)
                                    }
                                    else context.source.sendFeedback({ Text.literal("failed to load") }, false)
                                    1
                            }
                        )
                )
            })
        }
    }
}