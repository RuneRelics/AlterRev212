package gg.rsmod.plugins.api.dsl

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.weightedTableBuilder.TableTypes
import gg.rsmod.game.model.weightedTableBuilder.itemDrop
import gg.rsmod.plugins.api.NpcCombatBuilder


object WeightedTable {
    @DslMarker
    annotation class WeightedTableDslMarker
    @WeightedTableDslMarker
    class WeightedTableBuilder(val combatBuilder: NpcCombatBuilder) {
        /**
         * If position is not set it will mean drop where the npc died.
         */
        var position: Tile = Tile(0,0)

        open class TableBuilder(val combatBuilder: NpcCombatBuilder, val type: TableTypes, val tableRolls: Int = 1, val tableWeight: Int = 0) {
            fun add(itemid: Int, min: Int = 1, max: Int = 1, weight: Int = 0, description: String? = null, block: Unit.() -> Boolean = { true }) {
                combatBuilder.addDropToTable(
                    type = type,
                    item = itemDrop(
                        itemId = itemid,
                        min = min,
                        max = max,
                        weight = weight,
                        description = description,
                        block = block
                    ),
                    tableWeight = tableWeight,
                    tableRolls = tableRolls
                )
            }

        }


        fun always(init: TableBuilder.() -> Unit) {
            val builder = TableBuilder(combatBuilder, TableTypes.ALWAYS)
            builder.init()
        }

        fun preroll(tableWeight: Int, rolls: Int = 1, init: TableBuilder.() -> Unit) {
            val builder = TableBuilder(combatBuilder, TableTypes.PRE_ROLL, tableWeight = tableWeight, tableRolls = rolls)
            builder.init()
        }
        fun main(tableWeight: Int, rolls: Int = 1, init: TableBuilder.() -> Unit) {
            val builder = TableBuilder(combatBuilder, TableTypes.MAIN, tableWeight = tableWeight)
            builder.init()
        }



    }
}