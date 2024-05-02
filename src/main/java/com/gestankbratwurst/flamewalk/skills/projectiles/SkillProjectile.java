package com.gestankbratwurst.flamewalk.skills.projectiles;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.function.Predicate;

public abstract class SkillProjectile {

  protected final int maxTicksAlive;
  protected final Vector velocity;
  protected final double hitboxSize;
  protected final Predicate<Entity> hitCondition;
  protected Location location;
  protected int ticksAlive;
  @Getter
  protected boolean done;
  @Getter
  @Setter
  private UUID casterId;

  protected SkillProjectile(Location location, int maxTicksAlive, Vector velocity, double hitboxSize, Predicate<Entity> hitCondition) {
    this.maxTicksAlive = maxTicksAlive;
    this.velocity = velocity;
    this.hitboxSize = hitboxSize;
    this.location = location;
    this.hitCondition = hitCondition;
    this.done = false;
  }

  public boolean tick() {
    if (ticksAlive >= maxTicksAlive) {
      onRunout();
      done = true;
    } else {
      ticksAlive++;
      flyForward();
    }
    return isDone();
  }

  private void flyForward() {
    onFly();
    location.add(velocity);
    double distance = velocity.length();
    RayTraceResult result = location.getWorld().rayTrace(location, velocity, distance, FluidCollisionMode.NEVER, true, hitboxSize, hitCondition);

    if (result == null) {
      return;
    }

    done = true;

    if (result.getHitEntity() != null) {
      onHit((LivingEntity) result.getHitEntity());
    } else if (result.getHitBlock() != null) {
      onHit(result.getHitBlock());
    }
  }

  protected abstract void onFly();

  protected abstract void onHit(Block block);

  protected abstract void onHit(LivingEntity entity);

  protected abstract void onRunout();

}
