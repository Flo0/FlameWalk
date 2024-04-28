package com.gestankbratwurst.flamewalk.util;

import com.gestankbratwurst.flamewalk.FlameWalk;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class UtilLog {

  public static void logAction(Consumer<Logger> logAction) {
    FlameWalk plugin = JavaPlugin.getPlugin(FlameWalk.class);
    logAction.accept(plugin.getLogger());
  }

  public static void info(String message) {
    logAction(logger -> logger.info(message));
  }

}