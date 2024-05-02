package com.gestankbratwurst.flamewalk.game.impl.main;

import com.gestankbratwurst.flamewalk.nms.FlamewalkZombie;
import com.google.common.base.Preconditions;

public class ZombieWave extends SimpleMobWave {
  public ZombieWave(int mobCount, Nexus nexus) {
    super(mobCount, loc -> new FlamewalkZombie(loc));
    Preconditions.checkArgument(nexus != null, "Nexus cannot be null.");
  }
}
