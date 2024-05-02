package com.gestankbratwurst.flamewalk.items;

import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.api.tasks.TaskManager;
import com.gestankbratwurst.flamewalk.skills.SkillManager;
import com.gestankbratwurst.flamewalk.skills.projectiles.impl.Fireball;
import com.gestankbratwurst.flamewalk.skills.wand.FireRay;
import com.gestankbratwurst.flamewalk.skills.wand.WandSkillFactory;
import com.gestankbratwurst.flamewalk.skills.wand.WandSkills;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemManager {

  private final PersistentDataType<String, SkillIdComponent> skillIdType = new SkillDataType();

  public ItemManager(FlameWalk plugin) {
    Bukkit.getPluginManager().registerEvents(new ItemListener(this, plugin.getSkillManager()), plugin);
  }

  public boolean isWand(ItemStack itemStack) {
    if (itemStack == null) {
      return false;
    }
    ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) {
      return false;
    }
    PersistentDataContainer container = meta.getPersistentDataContainer();
    return container.has(NamespacedKey.minecraft("wand"), PersistentDataType.BYTE);
  }

  public void setWand(ItemStack itemStack) {
    itemStack.editMeta(meta -> {
      PersistentDataContainer container = meta.getPersistentDataContainer();
      container.set(NamespacedKey.minecraft("wand"), PersistentDataType.BYTE, (byte) 1);
    });
  }

  public ItemStack getWandItem(Player player) {
    Inventory inventory = player.getInventory();
    for (ItemStack itemStack : inventory) {
      if (isWand(itemStack)) {
        return itemStack;
      }
    }
    return null;
  }

  public void setSkills(ItemStack itemStack, String... skillIds) {
    SkillIdComponent skillIdComponent = new SkillIdComponent(skillIds);
    itemStack.editMeta(meta -> {
      PersistentDataContainer container = meta.getPersistentDataContainer();
      container.set(NamespacedKey.minecraft("skills"), skillIdType, skillIdComponent);
    });
  }

  public List<String> getSkills(ItemStack itemStack) {
    if (itemStack == null) {
      return null;
    }
    ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) {
      return null;
    }
    PersistentDataContainer container = meta.getPersistentDataContainer();
    SkillIdComponent skillIdComponent = container.get(NamespacedKey.minecraft("skills"), skillIdType);
    if (skillIdComponent == null) {
      return null;
    }
    return skillIdComponent.getSkillIds();
  }

  public void initializeWandSkillFactories(FlameWalk plugin) {
    SkillManager skillManager = plugin.getSkillManager();
    WandSkillFactory fireballFactory = new WandSkillFactory(plugin, WandSkills.FIREBALL_ID, (manager, caster, trigger) -> {
      Sound castSound = Sound.sound()
          .pitch(1.33F)
          .volume(0.7F)
          .type(org.bukkit.Sound.ITEM_FLINTANDSTEEL_USE)
          .build();
      Location soundLocation = caster.getLocation();
      caster.getWorld().playSound(castSound, soundLocation.x(), soundLocation.y(), soundLocation.z());
      Location location = caster.getEyeLocation();
      Vector direction = location.getDirection();
      location = location.add(direction.clone().multiply(0.66));
      direction = direction.normalize().multiply(1.33);
      Fireball fireball = new Fireball(location, direction);
      fireball.setCasterId(caster.getUniqueId());
      manager.getSkillProjectileManager().launchProjectile(fireball);
    }, 1000);
    skillManager.getSkillRegistry().addSkillFactory(fireballFactory);

    WandSkillFactory raySkill = new WandSkillFactory(plugin, WandSkills.FIRE_RAY_ID, (manager, caster, trigger) -> {
      Sound castSound = Sound.sound()
          .pitch(1.33F)
          .volume(0.6F)
          .type(org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_BLAST)
          .build();
      Location soundLocation = caster.getLocation();
      caster.getWorld().playSound(castSound, soundLocation.x(), soundLocation.y(), soundLocation.z());
      Location location = caster.getEyeLocation();
      Vector direction = location.getDirection();
      location = location.add(direction.clone().multiply(0.66));

      FireRay ray = new FireRay(5, 3, 64, 0.1, direction, location);
      ray.setCasterId(caster.getUniqueId());
      ray.cast();
    }, 700);
    skillManager.getSkillRegistry().addSkillFactory(raySkill);

    WandSkillFactory tripleRaySkill = new WandSkillFactory(plugin, WandSkills.TRIPLE_RAY_ID, (manager, caster, trigger) -> {
      Sound castSound = Sound.sound()
          .pitch(1.33F)
          .volume(0.6F)
          .type(org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_BLAST)
          .build();

      for (int i = 0; i < 3; i++) {
        TaskManager.runTaskLater(() -> {
          ThreadLocalRandom rnd = ThreadLocalRandom.current();
          Location soundLocation = caster.getLocation();
          caster.getWorld().playSound(castSound, soundLocation.x(), soundLocation.y(), soundLocation.z());
          Location location = caster.getEyeLocation();
          Vector direction = location.getDirection();
          Vector randomOffset = new Vector(rnd.nextDouble(-0.075, 0.075), rnd.nextDouble(-0.075, 0.075), rnd.nextDouble(-0.075, 0.075));
          location = location.add(direction.clone().multiply(0.66));

          FireRay ray = new FireRay(7, 3, 64, 0.1, direction.add(randomOffset), location);
          ray.setCasterId(caster.getUniqueId());
          ray.cast();
        }, i * 3);
      }

    }, 1000);
    skillManager.getSkillRegistry().addSkillFactory(tripleRaySkill);
  }

}
