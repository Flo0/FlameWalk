package com.gestankbratwurst.flamewalk.game.impl.main;

import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.cd.CooldownManager;
import com.gestankbratwurst.flamewalk.game.GameState;
import com.gestankbratwurst.flamewalk.game.eventbus.EventbusState;
import com.gestankbratwurst.flamewalk.game.impl.CleanupGameState;
import com.gestankbratwurst.flamewalk.items.ItemManager;
import com.gestankbratwurst.flamewalk.items.WandItems;
import com.gestankbratwurst.flamewalk.skills.SkillManager;
import com.gestankbratwurst.flamewalk.util.UtilMob;
import com.gestankbratwurst.flamewalk.util.UtilPlayer;
import com.gestankbratwurst.flamewalk.util.UtilString;
import com.gestankbratwurst.flamewalk.util.msg.Msg;
import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MainGameState extends EventbusState {

  @Getter
  private final Nexus nexus;

  private final Deque<MobWave> mobWaves;
  private final List<Location> waveSpawnLocations = new ArrayList<>();
  private int ticksAlive;
  private int spawnsPerWave = 2;
  private int waveCount = 0;
  private boolean done = false;
  private final BossBar bossBar;
  private int ticksToNextWave = 380;

  private final Map<UUID, Integer> kills;
  private final Map<UUID, Integer> wandUpgrades;

  public MainGameState(FlameWalk plugin) {
    super(plugin);
    Location spawn = plugin.getGameManager().getPlugin().getWorldManager().getGameWorld().getSpawnLocation();
    this.nexus = new Nexus(spawn.clone().add(0, 1.5, 0));
    this.mobWaves = new ArrayDeque<>();
    for (int i = 0; i < 2000; i++) {
      int mobs = 2 + i / 6;
      this.mobWaves.add(new ZombieWave(mobs, nexus));
    }
    this.waveSpawnLocations.add(spawn.clone().add(23, -1.25, 0));
    this.waveSpawnLocations.add(spawn.clone().add(-23, -1.25, 0));
    this.waveSpawnLocations.add(spawn.clone().add(0, -1.25, 23));
    this.waveSpawnLocations.add(spawn.clone().add(0, -1.25, -23));
    this.bossBar = Bukkit.createBossBar("§6Welle: §f" + waveCount, BarColor.YELLOW, BarStyle.SOLID);
    bossBar.setProgress(1.0);
    plugin.getGameManager().getActivePlayers().stream()
        .map(Bukkit::getPlayer)
        .filter(Objects::nonNull)
        .forEach(bossBar::addPlayer);
    bossBar.setVisible(true);
    this.kills = new HashMap<>();
    this.wandUpgrades = new HashMap<>();
    this.plugin.getGameManager().getActivePlayers().forEach(player -> {
      this.kills.put(player, 0);
      this.wandUpgrades.put(player, 0);
    });
  }

  @Override
  public void tick() {
    this.nexus.tick();
    this.ticksAlive++;

    double waveProgress = ticksToNextWave / 380D;
    bossBar.setProgress(waveProgress);

    if (ticksAlive % 6 == 0) {
      this.renderSpawnLocations();
    }

    CooldownManager cooldownManager = plugin.getCooldownManager();

    this.plugin.getGameManager().getActivePlayers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(player -> {
      long cooldown = cooldownManager.getActionCooldown(player.getUniqueId(), "WAND");
      long cooldownLeft = cooldownManager.getCooldownLeft(player.getUniqueId(), "WAND");

      double percentCd = cooldownLeft / (double) cooldown;

      String barRight = UtilString.progressBar(percentCd, 8, "§e", "§c", "|", false);
      String barLeft = UtilString.progressBar(percentCd, 8, "§e", "§c", "|", true);

      int neededKills = 15 + this.wandUpgrades.get(player.getUniqueId()) * 30;
      int kills = this.kills.get(player.getUniqueId());
      player.sendActionBar(Component.text(barLeft + " §f<  " + "§6Kills: §f" + kills + "§6/§f" + neededKills + "  §f> " + barRight));
    });

    ticksToNextWave--;
    if (ticksToNextWave <= 0) {
      ticksToNextWave = 320;
      for (int i = 0; i < spawnsPerWave; i++) {
        MobWave wave = this.mobWaves.poll();
        if (wave == null) {
          continue;
        }
        Location spawnLoc = randomSpawnLocation();
        wave.spawnAt(spawnLoc);
        Sound sound = Sound.sound()
            .pitch(0.75F)
            .volume(3.75F)
            .type(org.bukkit.Sound.ENTITY_LIGHTNING_BOLT_THUNDER)
            .build();

        spawnLoc.getWorld().playSound(sound, spawnLoc.x(), spawnLoc.y(), spawnLoc.z());
      }

      waveCount++;
      bossBar.setTitle("§6Welle: §f" + waveCount);
    }
  }

  private Location randomSpawnLocation() {
    return this.waveSpawnLocations.get(ThreadLocalRandom.current().nextInt(this.waveSpawnLocations.size()));
  }

  private void renderSpawnLocations() {
    float size = ThreadLocalRandom.current().nextFloat(0.5F, 1.5F);
    this.waveSpawnLocations.forEach(loc -> loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 1.5, 2.5, 1.5, new Particle.DustOptions(Color.RED, size)));
  }

  @Override
  protected void registerHandlers() {
    this.registerHandler(EntityDeathEvent.class, event -> {
      LivingEntity entity = event.getEntity();
      if (entity instanceof Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.setRespawnLocation(this.nexus.getCenter());
        player.spigot().respawn();
        return;
      }
      UUID killerId = UtilMob.getLastDamager(entity);
      if (killerId == null) {
        return;
      }
      int killCount = this.kills.compute(killerId, (id, kills) -> kills == null ? 1 : kills + 1);
      int wandUpgrades = this.wandUpgrades.get(killerId);
      int neededKills = 15 + wandUpgrades * 30;

      if (wandUpgrades > 3) {
        return;
      }

      if (killCount >= neededKills) {
        this.kills.put(killerId, 0);
        int totalUpgrades = this.wandUpgrades.compute(killerId, (id, upgrades) -> upgrades == null ? 1 : upgrades + 1);
        Player killer = Bukkit.getPlayer(killerId);

        if (killer == null) {
          return;
        }

        ItemStack wand = Optional.ofNullable(switch (totalUpgrades) {
          case 1 -> WandItems.FIRE_RAY_WAND;
          case 2 -> WandItems.TRIPLE_RAY_WAND;
          default -> {
            this.done = true;
            yield null;
          }
        }).map(sup -> sup.apply(plugin.getItemManager())).orElse(null);

        if (wand != null) {
          switchSkills(wand, plugin.getItemManager().getWandItem(killer), killer);
          killer.getInventory().clear();
          killer.getInventory().addItem(wand);
          Msg.info(killer, "Dein Zauberstab wurde verbessert!");
          UtilPlayer.playSuccessSound(killer);
        }
      }
    });
    this.registerHandler(FoodLevelChangeEvent.class, event -> event.setFoodLevel(20));
  }

  private void switchSkills(ItemStack targetItem, ItemStack previousItem, Player player) {
    SkillManager skillManager = plugin.getSkillManager();
    ItemManager itemManager = plugin.getItemManager();
    if (itemManager.isWand(previousItem)) {
      itemManager.getSkills(previousItem).forEach(skillId -> skillManager.removeSkill(player, skillId));
    }

    if (itemManager.isWand(targetItem)) {
      itemManager.getSkills(targetItem).forEach(skillId -> skillManager.addSkill(player, skillId));
    }
  }

  @Override
  public boolean isDone() {
    return this.done;
  }

  @Override
  public void onStart() {
    this.plugin.getGameManager().getActivePlayers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(player -> {
      player.setGameMode(GameMode.ADVENTURE);
      player.getInventory().clear();
      player.getInventory().addItem(WandItems.FIREBALL_WAND.apply(plugin.getItemManager()));
    });
  }

  @Override
  public void onEnd() {
    nexus.getCenter().getWorld().getEntities().forEach(entity -> {
      if (!(entity instanceof Player player)) {
        entity.remove();
      } else {
        Sound sound = Sound.sound()
            .pitch(1F)
            .volume(1F)
            .type(org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE)
            .build();
        player.playSound(sound, Sound.Emitter.self());
        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(2000), Duration.ofMillis(500));
        Title title  = Title.title(Component.text("§6Gewonnen"), Component.text(""), times);
        player.showTitle(title);
        player.setGameMode(GameMode.SPECTATOR);
      }
    });
  }

  @Override
  public GameState getNextState() {
    return new CleanupGameState(this.plugin);
  }

  @Override
  public void onCancel() {

  }

}
