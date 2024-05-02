package com.gestankbratwurst.flamewalk.items;

import com.gestankbratwurst.flamewalk.skills.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemListener implements Listener {

  private final ItemManager itemManager;
  private final SkillManager skillManager;

  public ItemListener(ItemManager itemManager, SkillManager skillManager) {
    this.itemManager = itemManager;
    this.skillManager = skillManager;
  }

  @EventHandler
  public void onSwitch(PlayerItemHeldEvent event) {
    int targetSlot = event.getNewSlot();
    int previousSlot = event.getPreviousSlot();

    PlayerInventory inventory = event.getPlayer().getInventory();
    ItemStack targetItem = inventory.getItem(targetSlot);
    ItemStack previousItem = inventory.getItem(previousSlot);

    switchSkills(targetItem, previousItem, event.getPlayer());
  }

  private void switchSkills(ItemStack targetItem, ItemStack previousItem, Player player) {
    if (itemManager.isWand(previousItem)) {
      itemManager.getSkills(previousItem).forEach(skillId -> skillManager.removeSkill(player, skillId));
    }

    if (itemManager.isWand(targetItem)) {
      itemManager.getSkills(targetItem).forEach(skillId -> skillManager.addSkill(player, skillId));
    }
  }

}
