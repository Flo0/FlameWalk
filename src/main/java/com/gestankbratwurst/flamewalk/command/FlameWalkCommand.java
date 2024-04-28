package com.gestankbratwurst.flamewalk.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.api.gui.GuiManager;
import com.gestankbratwurst.flamewalk.selector.GameSelectionGUI;
import com.gestankbratwurst.flamewalk.util.msg.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("flamewalk|fw")
public class FlameWalkCommand extends BaseCommand {

  private final FlameWalk plugin;

  public FlameWalkCommand(FlameWalk plugin) {
    this.plugin = plugin;
  }

  @Default
  public void onDefault(CommandSender sender) {
    sender.sendMessage("Hello, World!");
  }

  @Subcommand("klasse")
  public void onSelectClass(Player sender) {
    Msg.info(sender, "Wähle eine Klasse aus.");
  }

  @Subcommand("mitspieler")
  @CommandPermission("flamewalk.admin")
  public void onParticipant(Player sender) {
    GameSelectionGUI gui = new GameSelectionGUI(this.plugin.getGameManager());
    this.plugin.getGuiManager().initializeAndRegister(gui);
    gui.openFor(sender);
    Msg.info(sender, "Wähle deine Mitspieler aus.");
  }

}
