package us.timinc.mc.cobblemon.whiteout

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.cobblemon.mod.common.api.scheduling.afterOnServer
import com.cobblemon.mod.common.util.getPlayer
import net.fabricmc.api.ModInitializer
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageType
import us.timinc.mc.cobblemon.counter.config.ConfigBuilder
import us.timinc.mc.cobblemon.whiteout.config.WhiteoutConfig

object Whiteout : ModInitializer {
    const val MOD_ID = "cobblemon-whiteout"
    val config: WhiteoutConfig = ConfigBuilder.load(WhiteoutConfig::class.java, MOD_ID)

    val POKEBATTLE_DAMAGE_SOURCE: ResourceKey<DamageType> = ResourceKey.create(
        Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "pokebattle")
    )

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
                val playerParty = Cobblemon.storage.getParty(player)
                if (playerParty.all { it.currentHealth == 0 }) {
                    if (config.healTeam) {
                        afterOnServer(1, player.level()) {
                            playerParty.heal()
                        }
                    }
                    player.hurt(
                        if (!config.showMessage) player.damageSources().genericKill() else player.damageSources()
                            .source(
                                POKEBATTLE_DAMAGE_SOURCE
                            ), Float.MAX_VALUE
                    )
                }
            }
        }
    }
}