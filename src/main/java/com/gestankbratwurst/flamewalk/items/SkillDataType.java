package com.gestankbratwurst.flamewalk.items;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class SkillDataType implements PersistentDataType<String, SkillIdComponent> {

  @Override
  public @NotNull Class<String> getPrimitiveType() {
    return String.class;
  }

  @Override
  public @NotNull Class<SkillIdComponent> getComplexType() {
    return SkillIdComponent.class;
  }

  @Override
  public @NotNull String toPrimitive(@NotNull SkillIdComponent skillIdComponent, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
    return String.join("#", skillIdComponent.getSkillIds());
  }

  @Override
  public @NotNull SkillIdComponent fromPrimitive(@NotNull String string, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
    return new SkillIdComponent(string.split("#"));
  }
}
