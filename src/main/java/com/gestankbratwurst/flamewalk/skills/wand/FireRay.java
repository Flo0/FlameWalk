package com.gestankbratwurst.flamewalk.skills.wand;

import com.gestankbratwurst.flamewalk.util.UtilMob;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class FireRay {

  private static final double STEP_SIZE = 0.45D;

  private final double damage;
  private final int maxPenetrations;
  private final double range;
  private final double width;
  private final Vector direction;
  private final Location start;
  @Getter
  @Setter
  private UUID casterId;

  public FireRay(double damage, int maxPenetrations, double range, double width, Vector direction, Location start) {
    this.damage = damage;
    this.maxPenetrations = maxPenetrations;
    this.range = range;
    this.width = width;
    this.direction = direction;
    this.start = start;
  }

  public void cast() {
    drawRay();
    damageEntitiesInRay();
  }

  private void drawRay() {
    World world = start.getWorld();
    Location current = start.clone();
    Vector step = direction.clone().normalize().multiply(STEP_SIZE);
    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 145, 0), 0.775F);
    for (double dist = 0; dist < range; dist += STEP_SIZE) {
      world.spawnParticle(Particle.REDSTONE, current, 1, 0, 0, 0, 0, dustOptions);
      current.add(step);
    }
  }

  private void damageEntitiesInRay() {
    World world = start.getWorld();
    Set<LivingEntity> hitEntities = new HashSet<>();

    Predicate<Entity> collector = entity -> {
      if (hitEntities.size() >= this.maxPenetrations) {
        return true;
      }
      if (entity instanceof LivingEntity living) {
        hitEntities.add(living);
      }
      return false;
    };
    world.rayTrace(start, direction, 64, FluidCollisionMode.NEVER, true, this.width, collector);

    hitEntities.forEach(entity -> {
      Location entityLoc = entity.getLocation();
      entity.getWorld().spawnParticle(Particle.FLAME, entityLoc, 3, 0.3, 1, 0.3, 0.05);
      Sound sound = Sound.sound()
          .pitch(1.25F)
          .volume(0.7F)
          .type(org.bukkit.Sound.BLOCK_CANDLE_EXTINGUISH)
          .build();

      entity.getWorld().playSound(sound, entityLoc.x(), entityLoc.y(), entityLoc.z());
      entity.damage(this.damage);
      if (this.casterId != null) {
        UtilMob.setLastDamager(entity, this.casterId);
      }
    });
  }

}
