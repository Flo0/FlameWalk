package com.gestankbratwurst.flamewalk.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class UtilMob {

  public static void setLastDamager(Entity entity, UUID damagerId) {
    entity.getPersistentDataContainer().set(NamespacedKey.minecraft("last_damager"), PersistentDataType.STRING, damagerId.toString());
  }

  public static UUID getLastDamager(Entity entity) {
    String uuidString = entity.getPersistentDataContainer().get(NamespacedKey.minecraft("last_damager"), PersistentDataType.STRING);
    return uuidString == null ? null : UUID.fromString(uuidString);
  }

}
