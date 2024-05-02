package com.gestankbratwurst.flamewalk.items;

import com.gestankbratwurst.flamewalk.skills.wand.WandSkills;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface WandItems {

  Function<ItemManager, ItemStack> FIREBALL_WAND = (manager) -> {
    ItemStack wand = new ItemStack(Material.BLAZE_ROD);
    wand.editMeta(meta -> {
      meta.displayName(Component.text("Feurball-Zauberstab").color(NamedTextColor.WHITE).style(Style.style().decoration(TextDecoration.ITALIC, false)));
      List<Component> lore = new ArrayList<>();
      lore.add(Component.text(""));
      addLoreInfo(lore, 10D, 1, 1D, "Klein");
      lore.add(Component.text(""));
      lore.add(Component.text("Recktsklicken zum zaubern.").color(NamedTextColor.GRAY).style(Style.style().decoration(TextDecoration.ITALIC, false)));
      meta.lore(lore);
    });
    manager.setWand(wand);
    manager.setSkills(wand, WandSkills.FIREBALL_ID);
    return wand;
  };

  Function<ItemManager, ItemStack> FIRE_RAY_WAND = (manager) -> {
    ItemStack wand = new ItemStack(Material.BLAZE_ROD);
    wand.editMeta(meta -> {
      meta.displayName(Component.text("Feurstrahl-Zauberstab").color(NamedTextColor.YELLOW).style(Style.style().decoration(TextDecoration.ITALIC, false)));
      List<Component> lore = new ArrayList<>();
      lore.add(Component.text(""));
      addLoreInfo(lore, 5D, 1, 0.7D, "Klein");
      lore.add(Component.text("Durchschläge: ").style(Style.style().decoration(TextDecoration.ITALIC, false)).color(NamedTextColor.GRAY).append(Component.text(3).color(NamedTextColor.WHITE).style(Style.style().decoration(TextDecoration.ITALIC, false))));
      lore.add(Component.text(""));
      lore.add(Component.text("Recktsklicken zum zaubern.").style(Style.style().decoration(TextDecoration.ITALIC, false)).color(NamedTextColor.GRAY));
      meta.lore(lore);
    });
    manager.setWand(wand);
    manager.setSkills(wand, WandSkills.FIRE_RAY_ID);
    return wand;
  };

  Function<ItemManager, ItemStack> TRIPLE_RAY_WAND = (manager) -> {
    ItemStack wand = new ItemStack(Material.BLAZE_ROD);
    wand.editMeta(meta -> {
      meta.displayName(Component.text("Triple-Feurstrahl-Zauberstab").color(NamedTextColor.GOLD).style(Style.style().decoration(TextDecoration.ITALIC, false)));
      List<Component> lore = new ArrayList<>();
      lore.add(Component.text(""));
      addLoreInfo(lore, 7D, 3, 1.0D, "Klein");
      lore.add(Component.text("Durchschläge: ").color(NamedTextColor.GRAY).append(Component.text(3).color(NamedTextColor.WHITE).style(Style.style().decoration(TextDecoration.ITALIC, false))));
      lore.add(Component.text(""));
      lore.add(Component.text("Recktsklicken zum zaubern.").color(NamedTextColor.GRAY).style(Style.style().decoration(TextDecoration.ITALIC, false)));
      meta.lore(lore);
    });
    manager.setWand(wand);
    manager.setSkills(wand, WandSkills.TRIPLE_RAY_ID);
    return wand;
  };

  private static void addLoreInfo(List<Component> lore, double dmg, int projectiles, double cooldown, String size) {
    lore.add(Component.text("Schaden: ").style(Style.style().decoration(TextDecoration.ITALIC, false)).color(NamedTextColor.GRAY).append(Component.text("%.1f".formatted(dmg)).color(NamedTextColor.WHITE).style(Style.style().decoration(TextDecoration.ITALIC, false))));
    lore.add(Component.text("Projektile: ").style(Style.style().decoration(TextDecoration.ITALIC, false)).color(NamedTextColor.GRAY).append(Component.text(projectiles).color(NamedTextColor.WHITE).style(Style.style().decoration(TextDecoration.ITALIC, false))));
    lore.add(Component.text("Cooldown: ").style(Style.style().decoration(TextDecoration.ITALIC, false)).color(NamedTextColor.GRAY).append(Component.text("%.2fs".formatted(cooldown)).color(NamedTextColor.WHITE).style(Style.style().decoration(TextDecoration.ITALIC, false))));
    lore.add(Component.text("Größe: ").style(Style.style().decoration(TextDecoration.ITALIC, false)).color(NamedTextColor.GRAY).append(Component.text(size).color(NamedTextColor.WHITE).style(Style.style().decoration(TextDecoration.ITALIC, false))));
  }

}
