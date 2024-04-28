package com.gestankbratwurst.flamewalk.util;

import com.gestankbratwurst.flamewalk.api.tasks.TaskManager;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

public class UtilPlayer {

  public static void playUIClick(Player player) {
    Sound sound = Sound.sound()
        .pitch(0.75F)
        .volume(0.75F)
        .type(org.bukkit.Sound.UI_BUTTON_CLICK.key())
        .build();

    player.playSound(sound, Sound.Emitter.self());
  }

  public static void playSuccessSound(Player player) {
    Sound firstSound = Sound.sound()
        .pitch(1.0F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .build();

    Sound secondSound = Sound.sound()
        .pitch(1.33F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .build();
    player.playSound(firstSound, Sound.Emitter.self());

    TaskManager.runTaskLater(() -> player.playSound(secondSound, Sound.Emitter.self()), 4);
  }

  public static void playFailSound(Player player) {
    Sound firstSound = Sound.sound()
        .pitch(0.8F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .build();

    Sound secondSound = Sound.sound()
        .pitch(0.8F - 0.33F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .build();
    player.playSound(firstSound, Sound.Emitter.self());

    TaskManager.runTaskLater(() -> player.playSound(secondSound, Sound.Emitter.self()), 4);
  }

}
