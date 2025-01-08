package net.koteslaw

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState
import net.minecraft.world.World

class SaveData : PersistentState() {
    override fun writeNbt(nbt: NbtCompound?, registries: WrapperLookup?): NbtCompound {
         val mapNbt = NbtCompound()
         kits.forEach{ (key,value)->
            mapNbt.put(key,value.write(NbtList(),registries))
         }
         nbt!!.put(KEY,mapNbt)
         return nbt
    }
    companion object {
        var kits = mutableMapOf<String,PlayerInventory>()
        private const val KEY = "Duel.Kits"
        private fun PlayerInventory.write(nbtList: NbtList,registries: WrapperLookup?): NbtList {
            for (i in this.main.indices) {
                if (!this.main[i].isEmpty) {
                    val nbtCompound = NbtCompound()
                    nbtCompound.putByte("Slot", i.toByte())
                    nbtList.add(this.main[i].toNbt(registries, nbtCompound))
                }
            }
            for (ix in this.armor.indices) {
                if (!this.armor[ix].isEmpty) {
                    val nbtCompound = NbtCompound()
                    nbtCompound.putByte("Slot", (ix + 100).toByte())
                    nbtList.add(this.armor[ix].toNbt(registries, nbtCompound))
                }
            }
            for (ixx in this.offHand.indices) {
                if (!this.offHand[ixx].isEmpty) {
                    val nbtCompound = NbtCompound()
                    nbtCompound.putByte("Slot", (ixx + 150).toByte())
                    nbtList.add(this.offHand[ixx].toNbt(registries, nbtCompound))
                }
            }
            return nbtList
        }
        private fun PlayerInventory.read(nbtList: NbtList, registryLookup: WrapperLookup?) {
            this.main.clear()
            this.armor.clear()
            this.offHand.clear()
            for (i in nbtList.indices) {
                val nbtCompound = nbtList.getCompound(i)
                val j = nbtCompound.getByte("Slot").toInt() and 255
                val itemStack = ItemStack.fromNbt(registryLookup, nbtCompound)
                    .orElse(ItemStack.EMPTY) as ItemStack
                if (j >= 0 && j < this.main.size) {
                    this.main[j] = itemStack
                } else if (j >= 100 && j < this.armor.size + 100) {
                    this.armor[j - 100] = itemStack
                } else if (j >= 150 && j < this.offHand.size + 150) {
                    this.offHand[j - 150] = itemStack
                }
            }
        }
        private fun createFromNbt(tag: NbtCompound, registryLookup: WrapperLookup?): SaveData {
            val state = SaveData()
            val mapNbt = tag.getCompound(KEY)
            val map = mutableMapOf<String, PlayerInventory>()
            for (key in mapNbt.keys) {
                val inventory = PlayerInventory(null)
                val nbtList: NbtList = mapNbt.getList(key, NbtElement.COMPOUND_TYPE.toInt())
                inventory.read(nbtList,registryLookup)
                map[key] = inventory
            }
            this.kits = map
            return state
        }
        private val type = Type(
            { SaveData() },
            { tag: NbtCompound, registryLookup: WrapperLookup? ->
                createFromNbt(
                    tag,
                    registryLookup
                )
            },
            null
        )
        fun getServerState(server: MinecraftServer): SaveData {
            val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
            val state = persistentStateManager.getOrCreate(type,"duel")
            state.markDirty()
            return state
        }
    }
}