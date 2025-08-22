package dev.anvilcraft.lib.mixin;

import dev.anvilcraft.lib.event.ItemEntityEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity {
    @Unique
    private BlockPos anvillib$blockPos;

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tickReturn(CallbackInfo ci) {
        BlockPos blockPos = BlockPos.containing(this.position());
        if (!blockPos.equals(this.anvillib$blockPos)) {
            NeoForge.EVENT_BUS.post(new ItemEntityEvent.InToBlock(
                this.level(),
                (ItemEntity) (Object) this,
                blockPos,
                this.position(),
                this.getDeltaMovement()
            ));
        }
        this.anvillib$blockPos = blockPos;
    }
}
