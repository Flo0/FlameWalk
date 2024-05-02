package com.gestankbratwurst.flamewalk.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SkillRegistry<C> {

  public interface SkillFactory<C, T> extends Supplier<Skill<C, T>> {
    String getSkillId();
  }

  private final Map<String, SkillFactory<C, ?>> skillMap;

  public SkillRegistry() {
    skillMap = new HashMap<>();
  }

  public <T> void addSkillFactory(SkillFactory<C, T> skill) {
    skillMap.put(skill.getSkillId(), skill);
  }

  public Skill<C, ?> createSkill(final String id) {
    return skillMap.get(id).get();
  }

}
