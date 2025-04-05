package us.timinc.mc.cobblemon.whiteout

import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import net.fabricmc.api.ModInitializer

object Whiteout : ModInitializer {
    const val MOD_ID = "cobblemon-whiteout"

    override fun onInitialize() {
        CobblemonEvents.BATTLE_FAINTED.subscribe { handleBattleFainted(it) }
    }

    private fun handleBattleFainted(battleFaintedEvent: BattleFaintedEvent) {
        val killed = battleFaintedEvent.killed
        val entity = killed.entity ?: return
        val owner = entity.owner ?: return
        if (killed.actor.type != ActorType.PLAYER) return
        if (killed.actor.pokemonList.all { it.health == 0 }) {
            owner.kill()
        }
    }
}
