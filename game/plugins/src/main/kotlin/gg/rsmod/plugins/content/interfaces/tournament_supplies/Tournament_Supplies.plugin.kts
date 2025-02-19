package gg.rsmod.plugins.content.interfaces.tournament_supplies

import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.game.model.attr.INTERACTING_SLOT_ATTR
import gg.rsmod.game.model.priv.Privilege
import java.lang.NullPointerException

val itemListBySlot = hashMapOf<Int, Int>()

on_world_init {
    val itemEnum = world.definitions.get(EnumDef::class.java, 1124)
    itemEnum.values.forEach { it ->
        val item = it.value as Int
        val slot = it.key as Int
        itemListBySlot[slot] = item
    }
}



on_command("tournament", Privilege.ADMIN_POWER) {
    Tournament_Supplies.open(player)
}

on_button(interfaceId = Tournament_Supplies.TOURNAMENT_SUPPLIES_INTERFACE, component = 4) {
    val itemid = itemListBySlot[player.attr[INTERACTING_SLOT_ATTR]!!]!!
    val option = player.getInteractingOption()
    if (world.definitions.getCount(ItemDef::class.java) < itemid) {
        player.message("[Unhandled item] - $itemid")
    } else {
        if (option == 9) {
            world.sendExamine(player, itemid, type = ExamineEntityType.ITEM)
            return@on_button
        }
        var amount = when (option) {
            0 -> 1
            1 -> 5
            2 -> 10
            3 -> -1
            else -> return@on_button
        }
        if (amount == -1) {
            player.queue(TaskPriority.WEAK) {
                amount = inputInt("How many would you like to withdraw?")
                if (amount > 0) {
                    if (player.inventory.freeSlotCount < amount && !world.definitions.get(
                            ItemDef::class.java,
                            itemid
                        ).stackable
                    ) {
                        amount = player.inventory.freeSlotCount
                    }
                    player.inventory.add(itemid, amount)
                }
            }
        } else {
            if (world.definitions.get(ItemDef::class.java, itemid).stackable) {
                amount = 10000
            }
            player.inventory.add(itemid, amount)
        }
    }
}

on_button(interfaceId = Tournament_Supplies.TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE, component = 0) {
    try {
        val opt = player.getInteractingOption()
        val slot = player.attr[INTERACTING_SLOT_ATTR]!!
        if (opt == 9) {
            world.sendExamine(player, player.inventory[slot]!!.id, type = ExamineEntityType.ITEM)
            return@on_button
        }
        var amount = when(opt) {
            1 -> player.inventory.getItemCount(player.inventory[slot]!!.id)
            else -> 1
        }
        if (world.definitions.get(ItemDef::class.java, player.inventory[slot]!!.id).stackable) {
            amount = player.inventory.getItemCount(player.inventory[slot]!!.id)
        }
        player.inventory.remove(item = player.inventory[slot]!!.id, amount = amount , beginSlot = slot)
    } catch (_: NullPointerException) {
        // @TODO Ye dunno but some times when spamming to fast the click the ['Option'] goes null and console starts bitching
    }
}