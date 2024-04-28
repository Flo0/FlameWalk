package com.gestankbratwurst.flamewalk.world;

import com.gestankbratwurst.flamewalk.util.UtilLog;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.util.UUID;

public class WorldManager {

  private static final String WORLD_NAME = "flameworld";

  private UUID worldId;

  public UUID getWorldId() {
    return worldId;
  }

  public World getGameWorld() {
    World world = Bukkit.getWorld(this.worldId);
    Preconditions.checkState(world != null, "GameWorld is not loaded.");
    return world;
  }

  public void loadGameWorld() {
    WorldCreator worldCreator = new WorldCreator(WORLD_NAME);
    worldCreator.environment(World.Environment.NETHER);
    worldCreator.generateStructures(false);
    worldCreator.generator(new VoidChunkGenerator());
    worldCreator.type(WorldType.NORMAL);

    UtilLog.info("Loading world...");
    World world = worldCreator.createWorld();
    Preconditions.checkState(world != null, "World could not be loaded.");
    this.worldId = world.getUID();

    UtilLog.info("World successfully loaded.");
  }

}
