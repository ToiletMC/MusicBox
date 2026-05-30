package ru.spliterash.musicbox.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class FaceUtils {
    private final Set<BlockFace> searchFace = new HashSet<>();

    static {
        searchFace.add(BlockFace.EAST);
        searchFace.add(BlockFace.NORTH);
        searchFace.add(BlockFace.SOUTH);
        searchFace.add(BlockFace.WEST);
        searchFace.add(BlockFace.UP);
        searchFace.add(BlockFace.DOWN);
    }

    public <T extends BlockState> T getRelativeAround(Block block, Class<T> tClass) {
        for (BlockFace face : searchFace) {
            Block anotherBlock = block.getRelative(face);
            BlockState state = anotherBlock.getState();
            //noinspection ConstantConditions
            if (state == null)
                continue;
            if (tClass.isInstance(state))
                return tClass.cast(state);
        }
        return null;
    }
}
