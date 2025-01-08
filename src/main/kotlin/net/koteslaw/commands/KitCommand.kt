package net.koteslaw.commands



import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.koteslaw.SaveData
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

typealias Context = CommandContext<ServerCommandSource>


class KitCommand {
    companion object{
        fun register(){
            CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource?>, registryAccess: CommandRegistryAccess?, environment: RegistrationEnvironment? ->
                dispatcher.register(
                    literal("kit")
                        .requires{source -> source.hasPermissionLevel(2)}
                        .then(
                            literal("save")
                                .then(
                                    argument("save",StringArgumentType.word())
                                        .executes { context: Context ->
                                            save(context,StringArgumentType.getString(context,"save"))
                                            1
                                        }
                                )

                        )
                        .then(
                            literal("load")
                                .then(
                                    argument("load",KitArgumentType.create())
                                     .executes { context: Context ->
                                            load(context,KitArgumentType.getString(context,"load"))
                                            1
                                     }
                                )

                        )
                )
            })
        }
        private fun save(context: Context,kit:String){
            //val serverState: SaveData = SaveData.getServerState(context.source.server)
            val player = context.source.player
            if(player!=null){
                var inv = SaveData.kits[kit]
                if(inv==null){
                    inv = PlayerInventory(null)
                }
                inv.clone(player.inventory)
                SaveData.kits[kit] =  inv
                context.source.sendFeedback({ Text.literal("saved") }, false)
            }
        }
        private fun load(context: Context,kit:String){
            //val serverState: SaveData = SaveData.getServerState(context.source.server)
            val player = context.source.player
            val inventory = SaveData.kits[kit]
            if(inventory!=null){
                player!!.inventory.clone(inventory)
                context.source.sendFeedback({ Text.literal("loaded") }, false)
            }
            else context.source.sendFeedback({ Text.literal("failed to load") }, false)
        }
    }



}