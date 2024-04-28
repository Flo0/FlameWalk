package com.gestankbratwurst.flamewalk;

import co.aikar.commands.PaperCommandManager;
import com.gestankbratwurst.flamewalk.api.gui.GuiManager;
import com.gestankbratwurst.flamewalk.api.skins.PlayerSkinManager;
import com.gestankbratwurst.flamewalk.command.FlameWalkCommand;
import com.gestankbratwurst.flamewalk.game.GameManager;
import com.gestankbratwurst.flamewalk.world.WorldManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.logging.Logger;

public final class FlameWalk extends JavaPlugin {

  public static void log(Consumer<Logger> logAction) {
    FlameWalk plugin = JavaPlugin.getPlugin(FlameWalk.class);
    logAction.accept(plugin.getLogger());
  }

  @Getter
  private GuiManager guiManager;
  @Getter
  private PlayerSkinManager playerSkinManager;
  @Getter
  private WorldManager worldManager;
  @Getter
  private GameManager gameManager;

  @Override
  public void onEnable() {
    this.guiManager = new GuiManager(this);
    this.playerSkinManager = new PlayerSkinManager();
    this.worldManager = new WorldManager();
    this.gameManager = new GameManager(this);

    PaperCommandManager paperCommandManager = new PaperCommandManager(this);
    paperCommandManager.enableUnstableAPI("help");
    paperCommandManager.registerCommand(new FlameWalkCommand(this));

    this.worldManager.loadGameWorld();
  }

  @Override
  public void onDisable() {

  }

}
