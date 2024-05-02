package com.gestankbratwurst.flamewalk.skills.projectiles.impl;

import com.gestankbratwurst.flamewalk.skills.projectiles.SkillProjectile;
import com.gestankbratwurst.flamewalk.util.UtilMob;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class Fireball extends SkillProjectile {


  public Fireball(Location location, Vector velocity) {
    super(location, 40, velocity, 0.25, entity -> entity instanceof LivingEntity && !(entity instanceof Player));
  }

  @Override
  protected void onFly() {
    this.location.getWorld().spawnParticle(Particle.FLAME, this.location, 1, 0.025, 0.025, 0.025, 0.025);
    if (this.ticksAlive % 2 == 0) {
      this.location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.location, 1, 0.1, 0.1, 0.1, 0);
    }
  }

  @Override
  protected void onHit(Block block) {
    block.getWorld().spawnParticle(Particle.FLAME, this.location, 8, 0.3, 0.3, 0.3, 0.05);
    Sound sound = Sound.sound()
        .pitch(1.5F)
        .volume(0.7F)
        .type(org.bukkit.Sound.ENTITY_BLAZE_SHOOT)
        .build();
    block.getWorld().playSound(sound, this.location.x(), this.location.y(), this.location.z());
  }

  @Override
  protected void onHit(LivingEntity entity) {
    entity.getWorld().spawnParticle(Particle.FLAME, this.location, 8, 0.3, 0.3, 0.3, 0.05);
    entity.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.location, 4, 0.3, 0.66, 0.3, 0.05);
    Sound sound = Sound.sound()
        .pitch(0.5F)
        .volume(0.7F)
        .type(org.bukkit.Sound.ENTITY_BLAZE_SHOOT)
        .build();
    entity.getWorld().playSound(sound, this.location.x(), this.location.y(), this.location.z());
    entity.damage(5);
    if(this.getCasterId() != null) {
      UtilMob.setLastDamager(entity, this.getCasterId());
    }
  }

  @Override
  protected void onRunout() {
    this.location.getWorld().spawnParticle(Particle.FLAME, this.location, 8, 0.3, 0.3, 0.3, 0.05);
    Sound sound = Sound.sound()
        .pitch(0.5F)
        .volume(0.7F)
        .type(org.bukkit.Sound.ENTITY_BLAZE_SHOOT)
        .build();
    this.location.getWorld().playSound(sound, this.location.x(), this.location.y(), this.location.z());
  }
}
