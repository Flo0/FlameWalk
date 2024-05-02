package com.gestankbratwurst.flamewalk;

import co.aikar.commands.PaperCommandManager;
import com.gestankbratwurst.flamewalk.api.gui.GuiManager;
import com.gestankbratwurst.flamewalk.api.skins.PlayerSkinManager;
import com.gestankbratwurst.flamewalk.cd.CooldownManager;
import com.gestankbratwurst.flamewalk.command.FlameWalkCommand;
import com.gestankbratwurst.flamewalk.game.GameManager;
import com.gestankbratwurst.flamewalk.items.ItemManager;
import com.gestankbratwurst.flamewalk.skills.SkillManager;
import com.gestankbratwurst.flamewalk.world.WorldManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlameWalk extends JavaPlugin {

  @Getter
  private GuiManager guiManager;
  @Getter
  private PlayerSkinManager playerSkinManager;
  @Getter
  private WorldManager worldManager;
  @Getter
  private GameManager gameManager;
  @Getter
  private SkillManager skillManager;
  @Getter
  private ItemManager itemManager;
  @Getter
  private CooldownManager cooldownManager;

  @Override
  public void onEnable() {
    this.guiManager = new GuiManager(this);
    this.playerSkinManager = new PlayerSkinManager();
    this.worldManager = new WorldManager();
    this.gameManager = new GameManager(this);
    this.skillManager = new SkillManager(this);
    this.itemManager = new ItemManager(this);
    this.cooldownManager = new CooldownManager();

    PaperCommandManager paperCommandManager = new PaperCommandManager(this);
    paperCommandManager.enableUnstableAPI("help");
    paperCommandManager.registerCommand(new FlameWalkCommand(this));

    this.worldManager.loadGameWorld();
    this.itemManager.initializeWandSkillFactories(this);
  }

  @Override
  public void onDisable() {

  }


}
