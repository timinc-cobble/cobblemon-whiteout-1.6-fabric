package us.timinc.mc.cobblemon.whiteout

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.cobblemon.mod.common.util.getPlayer
import net.fabricmc.api.ModInitializer

object Whiteout : ModInitializer {
    const val MOD_ID = "cobblemon-whiteout"

    override fun onInitialize() {
        CobblemonEvents.BATTLE_VICTORY.subscribe { handleBattleFainted(it) }
    }

    private fun handleBattleFainted(evt: BattleVictoryEvent) {
        val allActors: MutableList<BattleActor> = mutableListOf()
        allActors.addAll(evt.losers)
        allActors.addAll(evt.winners)
        for (actor in allActors) {
            for (playerUUID in actor.getPlayerUUIDs()) {
                val player = playerUUID.getPlayer() ?: continue
                if (Cobblemon.storage.getParty(player).all { it.currentHealth == 0 }) {
                    player.kill()
                }
            }
        }
    }
}