package dev.anvilcraft.lib.recipe.outcome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.init.reicpe.LibRecipeOutcomeTypes;
import dev.anvilcraft.lib.recipe.util.InWorldRecipeContext;
import dev.anvilcraft.lib.util.CodecUtil;
import lombok.Getter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.phys.Vec3;

@Getter
public class ProduceExplosion implements IRecipeOutcome<ProduceExplosion> {
    /**
     * 爆炸中心点的位置偏移量
     */
    private final Vec3 offset;
    /**
     * 爆炸威力
     */
    private final float power;
    /**
     * 是否产生火焰
     */
    private final boolean fire;
    /**
     * 爆炸的方块交互类型
     */
    private final Level.ExplosionInteraction explodeInteraction;
    /**
     * 爆炸发生的概率，范围为0f到1.0f
     */
    public final NumberProvider chance;

    /**
     * 构建一个新的产生爆炸配方结果
     *
     * @param offset             爆炸中心点的位置偏移量
     * @param power              爆炸威力
     * @param fire               是否产生火焰
     * @param explodeInteraction 爆炸的方块交互类型
     * @param chance             爆炸发生的概率
     */
    public ProduceExplosion(Vec3 offset, float power, boolean fire, Level.ExplosionInteraction explodeInteraction, NumberProvider chance) {
        this.offset = offset;
        this.power = power;
        this.fire = fire;
        this.explodeInteraction = explodeInteraction;
        this.chance = chance;
    }

    /**
     * 获取配方结果类型
     *
     * @return 配方结果类型
     */
    @Override
    public Type getType() {
        return LibRecipeOutcomeTypes.PRODUCE_EXPLOSION.get();
    }

    /**
     * 接受配方上下文并爆炸
     *
     * @param ctx 配方上下文
     */
    @Override
    public void accept(InWorldRecipeContext ctx) {
        ServerLevel level = ctx.getLevel();
        Vec3 ctr = ctx.getPos().add(this.offset);
        level.explode(null, ctr.x(), ctr.y(), ctr.z(), this.power, this.fire, this.explodeInteraction);
    }

    public static class Type implements IRecipeOutcome.Type<ProduceExplosion> {
        /**
         * 编解码器
         */
        public static final MapCodec<ProduceExplosion> MAP_CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
            Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(ProduceExplosion::getOffset),
            Codec.FLOAT.fieldOf("power").forGetter(ProduceExplosion::getPower),
            Codec.BOOL.fieldOf("fire").forGetter(ProduceExplosion::isFire),
            Level.ExplosionInteraction.CODEC.fieldOf("interact").forGetter(ProduceExplosion::getExplodeInteraction),
            NumberProviders.CODEC.optionalFieldOf("chance", ConstantValue.exactly(1f)).forGetter(ProduceExplosion::getChance)
        ).apply(ins, ProduceExplosion::new));

        public static final Codec<ProduceExplosion> CODEC = MAP_CODEC.codec();

        /**
         * 流编解码器
         */
        public static final StreamCodec<RegistryFriendlyByteBuf, ProduceExplosion> STREAM_CODEC = CodecUtil.codec2Stream(Type.CODEC);

        /**
         * 获取MapCodec编解码器
         *
         * @return MapCodec编解码器
         */
        @Override
        public MapCodec<ProduceExplosion> codec() {
            return Type.MAP_CODEC;
        }

        /**
         * 获取StreamCodec编解码器
         *
         * @return StreamCodec编解码器
         */
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ProduceExplosion> streamCodec() {
            return Type.STREAM_CODEC;
        }
    }
}
