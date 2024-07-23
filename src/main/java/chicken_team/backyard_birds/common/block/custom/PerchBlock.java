package chicken_team.backyard_birds.common.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class PerchBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape EAST_HALF = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_HALF = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_HALF = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_HALF = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape FULL_X = Block.box(0D, 7D, 7D, 16D, 9D, 9D);
    protected static final VoxelShape FULL_Z = Block.box(7D, 7D, 0D, 9D, 9D, 16D);

    public PerchBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch (pState.getValue(FACING)) {
            case NORTH, SOUTH:
                return FULL_Z;
            case WEST, EAST:
            default:
                return FULL_X;
        }
    }

//    private boolean isFaceAttachable(Direction pDirection, BlockState pState){
//        return pState.getValue(FACING).getAxis() == pDirection.getAxis();
//    }
//
//    private boolean canAttachTo(BlockGetter pBlockReader, BlockPos pPos, Direction pDirection, BlockState pState) {
//        BlockState blockstate = pBlockReader.getBlockState(pPos);
//        return (blockstate.is(this) ? this.isFaceAttachable(pDirection, blockstate) : blockstate.isFaceSturdy(pBlockReader, pPos, pDirection));
//    }
//
//    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
//        Direction direction = pState.getValue(FACING);
//        return this.canAttachTo(pLevel, pPos.relative(direction.getOpposite()), direction, pState);
//    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
//        if (!pContext.replacingClickedOnBlock()) {
//            BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite()));
//            if (blockstate.is(this) && blockstate.getValue(FACING) == pContext.getClickedFace()) {
//                return null;
//            }
//        }

        BlockState blockstate1 = this.defaultBlockState();
        LevelReader levelreader = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();

        for(Direction direction : pContext.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                blockstate1 = blockstate1.setValue(FACING, direction.getOpposite());
                if (blockstate1.canSurvive(levelreader, blockpos)) {
                    return blockstate1;
                }
            }
        }

        return null;
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

}
