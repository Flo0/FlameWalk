package com.gestankbratwurst.flamewalk.skills.wand;

import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.cd.CooldownManager;
import com.gestankbratwurst.flamewalk.skills.Skill;
import com.gestankbratwurst.flamewalk.skills.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class WandSkill implements Skill<Player, PlayerInteractEvent> {

  @FunctionalInterface
  public interface WandAction {
    void cast(SkillManager manager, Player caster, PlayerInteractEvent trigger);
  }

  protected final FlameWalk plugin;
  private final WandAction castAction;
  private final int castCooldown;

  public WandSkill(FlameWalk plugin, WandAction castAction, int castCooldown) {
    this.plugin = plugin;
    this.castAction = castAction;
    this.castCooldown = castCooldown;
  }

  @Override
  public Class<PlayerInteractEvent> getTriggerType() {
    return PlayerInteractEvent.class;
  }

  @Override
  public void onAcquire(Player caster) {

  }

  @Override
  public void onRemove(Player caster) {

  }

  @Override
  public void onCast(Player caster, PlayerInteractEvent trigger) {
    UUID casterId = caster.getUniqueId();
    CooldownManager cooldownManager = plugin.getCooldownManager();
    cooldownManager.setActionCooldown(casterId, "WAND", this.castCooldown);
    long cooldownLeft = cooldownManager.getCooldownLeft(casterId, "WAND");
    if (cooldownLeft > 0) {
      return;
    }
    cooldownManager.setActionLastUse(casterId, "WAND");
    this.castAction.cast(this.plugin.getSkillManager(), caster, trigger);
  }
}
