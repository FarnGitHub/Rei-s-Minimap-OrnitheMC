package reifnsk.minimap.render;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;

public class BlockIDMetaData {

    public Block blockID;
    public int metaData;

    public BlockIDMetaData(Block id, int meta) {
        blockID = id;
        metaData = meta;

    }

}
