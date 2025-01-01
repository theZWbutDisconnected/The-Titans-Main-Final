/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.world;

import javax.annotation.Nullable;

import net.minecraft.block.PistonBlockStructureHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Base piston event, use {@link PistonEvent.Post} and {@link PistonEvent.Pre}
 */
public abstract class PistonEvent extends BlockEvent
{

    private final Direction direction;
    private final PistonMoveType moveType;

    /**
     * @param world
     * @param pos - The position of the piston
     * @param direction - The direction of the piston
     * @param direction - The move direction of the piston
     */
    public PistonEvent(World world, BlockPos pos, Direction direction, PistonMoveType moveType)
    {
        super(world, pos, world.getBlockState(pos));
        this.direction = direction;
        this.moveType = moveType;
    }

    /**
     * @return The direction of the piston block
     */
    public Direction getDirection()
    {
        return this.direction;
    }

    /**
     * Helper method that gets the piston position offset by its facing
     */
    public BlockPos getFaceOffsetPos()
    {
        return this.getPos().relative(direction);
    }

    /**
     * @return The movement type of the piston (extension, retraction)
     */
    public PistonMoveType getPistonMoveType()
    {
        return moveType;
    }

    /**
     * @return A piston structure helper for this movement. Returns null if the world stored is not a {@link World}
     */
    @Nullable
    public PistonBlockStructureHelper getStructureHelper()
    {
        if(this.getWorld() instanceof World) {
            return new PistonBlockStructureHelper((World) this.getWorld(), this.getPos(), this.getDirection(), this.getPistonMoveType().isExtend);
        } else {
            return null;
        }
    }

    /**
     * Fires after the piston has moved and set surrounding states. This will not fire if {@link PistonEvent.Pre} is cancelled.
     */
    public static class Post extends PistonEvent
    {

        public Post(World world, BlockPos pos, Direction direction, PistonMoveType moveType)
        {
            super(world, pos, direction, moveType);
        }

    }

    /**
     * Fires before the piston has updated block states. Cancellation prevents movement.
     */
    @Cancelable
    public static class Pre extends PistonEvent
    {

        public Pre(World world, BlockPos pos, Direction direction, PistonMoveType moveType)
        {
            super(world, pos, direction, moveType);
        }

    }

    public static enum PistonMoveType
    {
        EXTEND(true), RETRACT(false);

        public final boolean isExtend;

        PistonMoveType(boolean isExtend)
        {
            this.isExtend = isExtend;
        }
    }

}
