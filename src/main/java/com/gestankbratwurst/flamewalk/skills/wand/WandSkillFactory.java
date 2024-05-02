package com.gestankbratwurst.flamewalk.skills.wand;

import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.skills.Skill;
import com.gestankbratwurst.flamewalk.skills.SkillRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandSkillFactory implements SkillRegistry.SkillFactory<Player, PlayerInteractEvent> {

  private final FlameWalk plugin;
  private final String skillId;
  private final WandSkill.WandAction wandAction;
  private final int castCooldown;

  public WandSkillFactory(FlameWalk plugin, String skillId, WandSkill.WandAction wandAction, int castCooldown) {
    this.plugin = plugin;
    this.skillId = skillId;
    this.wandAction = wandAction;
    this.castCooldown = castCooldown;
  }

  @Override
  public String getSkillId() {
    return this.skillId;
  }

  @Override
  public Skill<Player, PlayerInteractEvent> get() {
    return new WandSkill(this.plugin, this.wandAction, this.castCooldown);
  }
}
