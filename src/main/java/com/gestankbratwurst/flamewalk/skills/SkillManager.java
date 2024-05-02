package com.gestankbratwurst.flamewalk.skills;

import com.gestankbratwurst.flamewalk.skills.projectiles.SkillProjectileManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SkillManager {

  @Getter
  private final SkillProjectileManager skillProjectileManager;
  @Getter
  private final SkillRegistry<Player> skillRegistry;
  private final Map<Player, Map<Class<?>, Map<String, Skill<Player, ?>>>> activeSkills;

  public SkillManager(JavaPlugin plugin) {
    skillRegistry = new SkillRegistry<>();
    activeSkills = new HashMap<>();
    this.skillProjectileManager = new SkillProjectileManager();
    Bukkit.getPluginManager().registerEvents(new SkillListener(this), plugin);
  }

  public void addSkill(Player player, String skillId) {
    Skill<Player, ?> skill = skillRegistry.createSkill(skillId);
    Map<Class<?>, Map<String, Skill<Player, ?>>> triggerMap = activeSkills.computeIfAbsent(player, p -> new HashMap<>());
    Map<String, Skill<Player, ?>> skillMap = triggerMap.computeIfAbsent(skill.getTriggerType(), t -> new HashMap<>());
    skillMap.put(skillId, skill);
    skill.onAcquire(player);
  }

  public void removeSkill(Player player, String skillId) {
    Map<Class<?>, Map<String, Skill<Player, ?>>> triggerMap = activeSkills.get(player);
    if (triggerMap == null) {
      return;
    }
    triggerMap.values().forEach(map -> map.remove(skillId));
  }

  public void terminate(Player player) {
    Map<Class<?>, Map<String, Skill<Player, ?>>> triggerMap = activeSkills.remove(player);
    if (triggerMap == null) {
      return;
    }
    triggerMap.values().forEach(map -> map.values().forEach(skill -> skill.onRemove(player)));
  }

  @SuppressWarnings("unchecked")
  public <T> void delegateSkillTrigger(Player player, T trigger) {
    Map<Class<?>, Map<String, Skill<Player, ?>>> triggerMap = activeSkills.get(player);
    if (triggerMap == null) {
      return;
    }
    Map<String, Skill<Player, ?>> skillMap = triggerMap.get(trigger.getClass());
    if (skillMap == null) {
      return;
    }
    skillMap.values().forEach(skill -> ((Skill<Player, T>) skill).onCast(player, trigger));
  }

}
