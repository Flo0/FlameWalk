package com.gestankbratwurst.flamewalk.nms;

import com.gestankbratwurst.flamewalk.game.impl.main.Nexus;
import net.kyori.adventure.sound.Sound;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;

public class AttackNexusGoal extends Goal {

  private final Nexus nexus;
  private final Mob mob;
  private long lastDamageTime;

  public AttackNexusGoal(Nexus nexus, Mob mob) {
    this.nexus = nexus;
    this.mob = mob;
  }

  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void tick() {
    tryDamageNexus();
  }

  @Override
  public void start() {
    tryDamageNexus();
  }

  private void tryDamageNexus() {
    if (System.currentTimeMillis() - this.lastDamageTime < 2000) {
      return;
    }

    this.lastDamageTime = System.currentTimeMillis();
    Sound sound = Sound.sound()
        .pitch(0.75F)
        .volume(2F)
        .type(org.bukkit.Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR)
        .build();

    Location center = nexus.getCenter();
    center.getWorld().playSound(sound, center.x(), center.y(), center.z());

    nexus.damage(mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).getValue() * 1.5D);
  }

  @Override
  public boolean canUse() {
    Location center = nexus.getCenter();
    return mob.position().distanceTo(new Vec3(center.x(), center.y(), center.z())) <= 4.5;
  }
}
