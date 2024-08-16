package chicken_team.backyard_birds.common.block.custom;

import chicken_team.backyard_birds.common.items.BBItems;
import chicken_team.backyard_birds.util.BBTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BeetleBarrelBlock extends Block {
    public BeetleBarrelBlock(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        Ingredient FOOD_ITEMS = Ingredient.of(BBTags.Items.BEETLE_FOOD);

        if (FOOD_ITEMS.test(itemstack)) {
            this.usePlayerItem(pPlayer, pHand, itemstack);
            extractProduce(pPlayer, pState, pLevel, pPos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    protected void usePlayerItem(Player pPlayer, InteractionHand pHand, ItemStack pStack) {
        if (!pPlayer.getAbilities().instabuild) {
            pStack.shrink(1);
        }
    }

    public static BlockState extractProduce(Entity pEntity, BlockState pState, Level pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide) {
            Vec3 vec3 = Vec3.atLowerCornerWithOffset(pPos, 0.5D, 1.01D, 0.5D).offsetRandom(pLevel.random, 0.7F);
            ItemEntity itementity = new ItemEntity(pLevel, vec3.x(), vec3.y(), vec3.z(), new ItemStack(Items.BONE_MEAL));
            itementity.setDefaultPickUpDelay();
            pLevel.addFreshEntity(itementity);
        }

        BlockState blockstate = empty(pEntity, pState, pLevel, pPos);
        pLevel.playSound((Player)null, pPos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1.0F, 1.0F);
        pLevel.playSound((Player)null, pPos, SoundEvents.PLAYER_BURP, SoundSource.BLOCKS, 1.0F, 1.0F);
        pLevel.playSound((Player)null, pPos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        return blockstate;
    }

    static BlockState empty(@Nullable Entity pEntity, BlockState pState, LevelAccessor pLevel, BlockPos pPos) {
        BlockState blockstate = pState;
        pLevel.setBlock(pPos, blockstate, 3);
        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pEntity, blockstate));
        return blockstate;
    }
}
