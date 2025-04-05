package us.timinc.mc.cobblemon.whiteout.mixin;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.world.gamerules.CobblemonGameRules;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 100000)
public class WhiteoutPlayerMixin {
    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void TimIncMcWhiteout$isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        MinecraftServer server = Cobblemon.implementation.server();
        if (server == null) {
            return;
        }
        if (server.getGameRules() == null) {
            return;
        }
        GameRules.BooleanRule battleInvulRule = server.getGameRules().get(CobblemonGameRules.BATTLE_INVULNERABILITY);
        if (battleInvulRule.get()) {
            cir.setReturnValue(false);
        }
    }
}
