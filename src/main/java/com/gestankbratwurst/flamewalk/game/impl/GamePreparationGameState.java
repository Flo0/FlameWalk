package com.gestankbratwurst.flamewalk.game.impl;

import com.gestankbratwurst.flamewalk.api.tasks.TaskManager;
import com.gestankbratwurst.flamewalk.game.GameManager;
import com.gestankbratwurst.flamewalk.game.GameState;
import com.gestankbratwurst.flamewalk.game.eventbus.EventbusState;
import com.gestankbratwurst.flamewalk.game.impl.main.MainGameState;
import com.gestankbratwurst.flamewalk.util.animation.SlideInStringAnimation;
import com.gestankbratwurst.flamewalk.util.msg.Msg;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePreparationGameState extends EventbusState {

  private static final String TITLE = Msg.getServerPrefix();
  private static final String[] SLIDE_COMPONENTS = {
      "<color:#FB150E>F",
      "<color:#FB2711>l",
      "<color:#FC3914>a",
      "<color:#FC4B17>m",
      "<color:#FD5D1A>m",
      "<color:#FD701D>e",
      "<color:#FD821F>n",
      "<color:#FE9422>l",
      "<color:#FEA625>a",
      "<color:#FFB828>u",
      "<color:#FFCA2B>f"
  };

  private final int maxTicks;
  private final SlideInStringAnimation titleAnimation;
  private final Sound secondChangeSound;
  private int ticksAlive = 0;
  private int lastSecond = 0;

  public GamePreparationGameState(GameManager gameManager) {
    super(gameManager);
    this.titleAnimation = new SlideInStringAnimation(Arrays.asList(SLIDE_COMPONENTS), 11);
    this.maxTicks = this.titleAnimation.getFrameCount() + 4 * 20;
    this.secondChangeSound = Sound.sound()
        .pitch(0.8F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_HAT)
        .build();
  }

  @Override
  public void tick() {
    this.ticksAlive++;
    Component topText;
    Component bottomText;
    boolean secondChanged;

    if (this.titleAnimation.isDone()) {
      topText = MiniMessage.miniMessage().deserialize(TITLE);
      int secondsLeft = Math.max(0, 3 - ((this.ticksAlive - titleAnimation.getFrameCount()) / 20));
      if (secondsLeft != this.lastSecond) {
        this.lastSecond = secondsLeft;
        secondChanged = true;
      } else {
        secondChanged = false;
      }
      bottomText = Component.text(secondsLeft);
    } else {
      secondChanged = false;
      topText = MiniMessage.miniMessage().deserialize(this.titleAnimation.nextFrame());
      bottomText = Component.text("");
    }

    Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofMillis(500), Duration.ZERO);

    Title title = Title.title(topText, bottomText, times);

    this.gameManager.getActivePlayers().stream()
        .map(Bukkit::getPlayer)
        .filter(Objects::nonNull)
        .forEach(player -> {
          player.showTitle(title);
          if(secondChanged) {
            player.playSound(this.secondChangeSound, Sound.Emitter.self());
          }
        });
  }

  @Override
  protected void registerHandlers() {

  }

  @Override
  public boolean isDone() {
    return this.ticksAlive >= this.maxTicks;
  }

  @Override
  public void onStart() {
    Sound[] sounds = {
        Sound.sound()
            .pitch(1.33F)
            .volume(1.0F)
            .type(org.bukkit.Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE)
            .build(),
        Sound.sound()
            .pitch(0.66F)
            .volume(1.0F)
            .type(org.bukkit.Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE)
            .build(),
        Sound.sound()
            .pitch(0.33F)
            .volume(1.0F)
            .type(org.bukkit.Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE)
            .build(),
        Sound.sound()
            .pitch(0.5F)
            .volume(1.0F)
            .type(org.bukkit.Sound.BLOCK_FURNACE_FIRE_CRACKLE)
            .build(),
        Sound.sound()
            .pitch(0.25F)
            .volume(1.0F)
            .type(org.bukkit.Sound.BLOCK_FURNACE_FIRE_CRACKLE)
            .build(),
        Sound.sound()
            .pitch(0.75F)
            .volume(1.0F)
            .type(org.bukkit.Sound.BLOCK_FIRE_AMBIENT)
            .build(),
        Sound.sound()
            .pitch(0.6F)
            .volume(1.0F)
            .type(org.bukkit.Sound.ENTITY_ENDER_DRAGON_SHOOT)
            .build()
    };

    this.gameManager.getActivePlayers().stream()
        .map(Bukkit::getPlayer)
        .filter(Objects::nonNull)
        .forEach(player -> Arrays.asList(sounds).forEach(sound -> player.playSound(sound, Sound.Emitter.self())));
  }

  @Override
  public void onEnd() {
    Sound sound = Sound.sound()
        .pitch(0.8F)
        .volume(1.0F)
        .type(org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL)
        .build();
    AtomicInteger counter = new AtomicInteger(1);
    Location teleportLocation = this.gameManager.getPlugin().getWorldManager().getGameWorld().getSpawnLocation();
    this.gameManager.getActivePlayers().stream()
        .map(Bukkit::getPlayer)
        .filter(Objects::nonNull)
        .forEach(player -> {
          player.playSound(sound, Sound.Emitter.self());
          TaskManager.runTaskLater(() -> player.teleportAsync(teleportLocation), 20 + counter.getAndIncrement());
        });
  }

  @Override
  public GameState getNextState() {
    return new MainGameState(this.gameManager);
  }

  @Override
  public void onCancel() {

  }
}
