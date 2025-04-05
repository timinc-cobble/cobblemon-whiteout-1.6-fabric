package us.timinc.mc.cobblemon.whiteout

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
        for (loser in evt.losers) {
            if (loser.pokemonList.all { it.health == 0 }) {
                for (playerUUID in loser.getPlayerUUIDs()) {
                    playerUUID.getPlayer()?.kill()
                }
            }
        }
    }
}