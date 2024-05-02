package com.gestankbratwurst.flamewalk.game.impl.main;

import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;

import java.util.function.Function;

public class SimpleMobWave implements MobWave {

  private final int mobCount;
  private final Function<Location, LivingEntity> mobSupplier;

  public SimpleMobWave(int mobCount, Function<Location, LivingEntity> mobSupplier) {
    this.mobCount = mobCount;
    this.mobSupplier = mobSupplier;
  }

  @Override
  public void spawnAt(Location location) {
    World world = location.getWorld();
    for (int i = 0; i < mobCount; i++) {
      LivingEntity nmsEntity = mobSupplier.apply(location);
      ((CraftWorld) world).getHandle().addFreshEntity(nmsEntity);
    }
  }
}
