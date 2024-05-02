package com.gestankbratwurst.flamewalk.items;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SkillIdComponent {

  private final Set<String> skillIds;

  public SkillIdComponent(String... strings) {
    this.skillIds = new HashSet<>(Arrays.asList(strings));
  }

  public List<String> getSkillIds() {
    return List.copyOf(this.skillIds);
  }

  public void addSkillId(String skillId) {
    this.skillIds.add(skillId);
  }

  public void removeSkillId(String skillId) {
    this.skillIds.remove(skillId);
  }

}
