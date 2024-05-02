package com.gestankbratwurst.flamewalk.nms;

import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.game.GameState;
import com.gestankbratwurst.flamewalk.game.impl.main.MainGameState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;

public class FlamewalkZombie extends Zombie {

  public FlamewalkZombie(Location location) {
    super(EntityType.ZOMBIE, ((CraftWorld) location.getWorld()).getHandle());
    this.setPos(location.getX(), location.getY(), location.getZ());

  }

  @Override
  protected void addBehaviourGoals() {
    GameState state = JavaPlugin.getPlugin(FlameWalk.class).getGameManager().getCurrentState();
    this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
    if (state instanceof MainGameState main) {
      this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(17.5D);
      this.goalSelector.addGoal(3, new AttackNexusGoal(main.getNexus(), this));
      this.goalSelector.addGoal(6, new WalkToNexusGoal(this, 1.0, 32, main.getNexus()));
    }
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
  }

}
