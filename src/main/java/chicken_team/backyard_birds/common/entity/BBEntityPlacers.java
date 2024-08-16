package chicken_team.backyard_birds.common.entity;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class BBEntityPlacers {
    public  static void entityPlacement() {

        SpawnPlacements.register(BBEntities.BBCHICKEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BBChicken::checkAnimalSpawnRules);

    }
}
