package com.gestankbratwurst.flamewalk.util;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ItemBuilder {

  public static ItemBuilder of(Material material) {
    return new ItemBuilder(material);
  }

  public static ItemBuilder of(ItemStack itemStack) {
    return new ItemBuilder(itemStack);
  }

  private Material material;
  private ItemMeta itemMeta;
  private int amount;
  private final List<Component> loreLines;

  public ItemBuilder(final Material material) {
    this(new ItemStack(material));
  }

  public ItemBuilder(final ItemStack itemStack) {
    this.itemMeta = itemStack.getItemMeta();
    this.material = itemStack.getType();
    this.amount = itemStack.getAmount();
    if (this.itemMeta.hasLore()) {
      this.loreLines = Lists.newArrayList(Objects.requireNonNull(this.itemMeta.lore()));
    } else {
      this.loreLines = Lists.newArrayList();
    }
  }

  public ItemBuilder clearLore() {
    this.editLore(List::clear);
    return this;
  }

  public ItemBuilder setDamagePercent(double percent) {
    percent = Math.max(0, percent);
    percent = Math.min(1.0, percent);

    if (itemMeta instanceof Damageable damageable) {
      damageable.setDamage((int) (percent * material.getMaxDurability()));
    }

    return this;
  }

  public ItemBuilder editLore(final Consumer<List<Component>> loreConsumer) {
    loreConsumer.accept(this.loreLines);
    return this;
  }

  public ItemBuilder applyAsPotion(final Consumer<PotionMeta> metaConsumer) {
    if (this.itemMeta instanceof PotionMeta potionMeta) {
      metaConsumer.accept(potionMeta);
    }
    return this;
  }

  public ItemBuilder applyAsFirework(final Consumer<FireworkMeta> metaConsumer) {
    if (this.itemMeta instanceof FireworkMeta fireworkMeta) {
      metaConsumer.accept(fireworkMeta);
    }
    return this;
  }

  public ItemBuilder name(Component component) {
    Component nonItalic = component.style(Style.style().decoration(TextDecoration.ITALIC, false).build());
    this.itemMeta.displayName(nonItalic);
    return this;
  }

  public ItemBuilder amount(final int amount) {
    this.amount = amount;
    return this;
  }

  public ItemBuilder meta(final ItemMeta meta) {
    this.itemMeta = meta;
    return this;
  }

  public ItemBuilder material(final Material material) {
    this.material = material;
    return this;
  }

  public ItemBuilder name(final String name) {
    this.itemMeta.displayName(Component.text(name).style(Style.style().decoration(TextDecoration.ITALIC, false).build()));
    return this;
  }

  public ItemBuilder lore(final String line) {
    Component nonItalic = Component.text(line).style(Style.style().decoration(TextDecoration.ITALIC, false).build());
    this.loreLines.add(nonItalic);
    return this;
  }

  public ItemBuilder lore(final Component line) {
    Component nonItalic = line.style(Style.style().decoration(TextDecoration.ITALIC, false).build());
    this.loreLines.add(nonItalic);
    return this;
  }

  public ItemBuilder lore(final String... lines) {
    Arrays.stream(lines).forEach(this::lore);
    return this;
  }

  public ItemBuilder lore(final Component... lines) {
    Arrays.stream(lines).forEach(this::lore);
    return this;
  }

  public ItemBuilder lore(final Collection<TextComponent> lines) {
    lines.forEach(this::lore);
    return this;
  }

  public ItemBuilder attribute(final Attribute attribute, final AttributeModifier modifier) {
    this.itemMeta.addAttributeModifier(attribute, modifier);
    return this;
  }

  public ItemBuilder enchant(final Enchantment enchantmen, final int level) {
    this.itemMeta.addEnchant(enchantmen, level, true);
    return this;
  }

  public ItemBuilder flag(final ItemFlag... flags) {
    this.itemMeta.addItemFlags(flags);
    return this;
  }

  public ItemBuilder setUnbreakable(final boolean value) {
    this.itemMeta.setUnbreakable(value);
    return this;
  }

  public ItemBuilder modelData(final int customModelData) {
    this.itemMeta.setCustomModelData(customModelData);
    return this;
  }

  public ItemStack build() {
    final ItemStack item = new ItemStack(this.material);
    if (!this.loreLines.isEmpty()) {
      this.itemMeta.lore(this.loreLines);
    }
    item.setItemMeta(this.itemMeta);
    if (this.amount != 1) {
      item.setAmount(this.amount);
    }
    return item;
  }

}
