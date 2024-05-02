package com.gestankbratwurst.flamewalk.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.items.ItemManager;
import com.gestankbratwurst.flamewalk.items.WandItems;
import com.gestankbratwurst.flamewalk.selector.GameSelectionGUI;
import com.gestankbratwurst.flamewalk.util.msg.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("flamewalk|fw")
public class FlameWalkCommand extends BaseCommand {

  private final FlameWalk plugin;

  public FlameWalkCommand(FlameWalk plugin) {
    this.plugin = plugin;
  }

  @Default
  public void onDefault(CommandSender sender) {
    Msg.info(sender, "FlameWalk Befehle:");
    Msg.info(sender, "/flamewalk mitspieler - Wähle deine Mitspieler aus.");
  }

  @Subcommand("mitspieler")
  @CommandPermission("flamewalk.admin")
  public void onParticipant(Player sender) {
    GameSelectionGUI gui = new GameSelectionGUI(this.plugin.getGameManager());
    this.plugin.getGuiManager().initializeAndRegister(gui);
    gui.openFor(sender);
    Msg.info(sender, "Wähle deine Mitspieler aus.");
  }

  @Subcommand("debug wands feuerball")
  @CommandPermission("flamewalk.admin")
  public void onWand(Player sender) {
    ItemManager itemManager = plugin.getItemManager();
    ItemStack wand = WandItems.FIREBALL_WAND.apply(itemManager);
    sender.getInventory().addItem(wand);
  }

  @Subcommand("debug wands feurray")
  @CommandPermission("flamewalk.admin")
  public void onWandRay(Player sender) {
    ItemManager itemManager = plugin.getItemManager();
    ItemStack wand = WandItems.FIRE_RAY_WAND.apply(itemManager);
    sender.getInventory().addItem(wand);
  }

  @Subcommand("debug wands tripleray")
  @CommandPermission("flamewalk.admin")
  public void onWandTripleRay(Player sender) {
    ItemManager itemManager = plugin.getItemManager();
    ItemStack wand = WandItems.TRIPLE_RAY_WAND.apply(itemManager);
    sender.getInventory().addItem(wand);
  }

}
