package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpLoc4Message(val item: Int, val x: Int, val z: Int, val movementType: Int) : Message