package com.gestankbratwurst.flamewalk.selector;

import com.gestankbratwurst.flamewalk.api.gui.abstraction.GuiButton;
import com.gestankbratwurst.flamewalk.api.gui.baseimpl.DynamicAbstractGUI;
import com.gestankbratwurst.flamewalk.api.tasks.TaskManager;
import com.gestankbratwurst.flamewalk.game.GameManager;
import com.gestankbratwurst.flamewalk.util.ItemBuilder;
import com.gestankbratwurst.flamewalk.util.UtilHead;
import com.gestankbratwurst.flamewalk.util.UtilPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class GameSelectionGUI extends DynamicAbstractGUI {

  private final GameManager gameManager;

  public GameSelectionGUI(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  @Override
  protected Inventory createInventory() {
    Component swordComponent = Component.text("⚔").color(TextColor.color(255, 128, 0));
    Component title = Component.text()
        .append(swordComponent)
        .append(Component.text(" Wähle Mitspieler aus ").color(NamedTextColor.WHITE))
        .append(swordComponent)
        .build();
    return Bukkit.createInventory(null, 9 * 6, title);
  }

  @Override
  protected void setupButtons() {
    Bukkit.getOnlinePlayers()
        .stream()
        .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
        .map(this::createSelectionButton).forEach(this::addButton);
    this.setButton(49, this.createStartButton());
  }

  private GuiButton createSelectionButton(Player player) {
    return GuiButton.builder()
        .iconCreator(() -> createSelectionIcon(player))
        .eventConsumer(event -> {
          UtilPlayer.playUIClick((Player) event.getWhoClicked());
          this.toggleSelection(player);
          this.decorate();
        }).build();
  }

  private ItemStack createSelectionIcon(Player player) {
    boolean selected = isPlayerSelected(player);
    Component playerName = Component.text(player.getName()).color(selected ? NamedTextColor.GREEN : NamedTextColor.RED);

    ItemBuilder builder = ItemBuilder.of(UtilHead.ofPlayer(player))
        .name(playerName)
        .lore(Component.text(""))
        .lore(Component.text("Klicke um den Spieler auszuwählen.").color(NamedTextColor.GRAY).decorate());

    if (selected) {
      builder.enchant(Enchantment.ARROW_DAMAGE, 1).flag(ItemFlag.HIDE_ENCHANTS);
    }

    return builder.build();
  }

  private GuiButton createStartButton() {
    return GuiButton.builder()
        .iconCreator(this::createStartIcon)
        .eventConsumer(event -> {
          UtilPlayer.playUIClick((Player) event.getWhoClicked());
          this.startGame();
          TaskManager.runTask(() -> event.getWhoClicked().closeInventory());
        })
        .asyncCreated(false)
        .build();
  }

  private ItemStack createStartIcon() {
    int selectedPlayerCount = getSelectedPlayerCount();
    int totalPlayerCount = getTotalPlayerCount();

    Component playerCountLine = Component.text()
        .append(Component.text("Spieler: ").color(NamedTextColor.GRAY).decorate())
        .append(Component.text(selectedPlayerCount + "/" + totalPlayerCount).color(NamedTextColor.WHITE).decorate())
        .build();

    return ItemBuilder.of(UtilHead.RED_ORB.get())
        .name(Component.text("Starte das Spiel").color(NamedTextColor.GREEN).decorate())
        .lore(Component.text(""))
        .lore(playerCountLine)
        .lore(Component.text(""))
        .lore(Component.text("Klicke hier um das Spiel zu starten.").color(NamedTextColor.GRAY).decorate())
        .build();
  }

  private int getSelectedPlayerCount() {
    return this.gameManager.getActivePlayerCount();
  }

  private int getTotalPlayerCount() {
    return Bukkit.getOnlinePlayers().size();
  }

  private boolean isPlayerSelected(Player player) {
    return this.gameManager.isActivePlayer(player.getUniqueId());
  }

  private void toggleSelection(Player player) {
    if (isPlayerSelected(player)) {
      this.gameManager.removeActivePlayer(player.getUniqueId());
    } else {
      this.gameManager.addActivePlayer(player.getUniqueId());
    }
  }

  private void startGame() {
    this.gameManager.start();
  }

}