package com.gestankbratwurst.flamewalk.game.impl.main;

import com.gestankbratwurst.flamewalk.util.UtilHead;
import com.gestankbratwurst.flamewalk.util.UtilString;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;

import java.util.concurrent.ThreadLocalRandom;

public class Nexus {

  private final TextDisplay textDisplay;
  private final ItemDisplay itemDisplay;
  @Getter
  private final double maxHealth;
  @Getter
  private double health;
  private int ticksAlive;

  public Nexus(Location location) {
    location = location.set(-2, 90, 0);
    this.maxHealth = 2000D;
    this.health = maxHealth;
    this.itemDisplay = location.getWorld().spawn(location.clone().add(-0.5, 1.75, 0.5), ItemDisplay.class, display -> {
      display.setPersistent(false);
      ItemStack itemStack = UtilHead.RED_ORB.get();
      display.setItemStack(itemStack);
      display.setGlowing(true);
      display.setGlowColorOverride(Color.AQUA);
    });
    this.textDisplay = location.getWorld().spawn(location.clone().add(-0.5, 2.15, 0.5), TextDisplay.class, display -> {
      display.setPersistent(false);
      display.setAlignment(TextDisplay.TextAlignment.CENTER);
      display.setBillboard(Display.Billboard.VERTICAL);
      display.setLineWidth(55);
      decorateTextDisplay(display);
    });
  }

  private void decorateTextDisplay(TextDisplay display) {
    String healthBar = "§7[" + UtilString.progressBar(health, maxHealth, 20, "§a", "§c", "|") + "§7]";
    Component lines = Component.text()
        .append(Component.text("Nexus").color(TextColor.color(200, 25, 0))).appendNewline()
        .append(Component.text(((int) health) + " HP")).appendNewline()
        .append(Component.text(healthBar))
        .build();

    display.text(lines);
  }

  public void tick() {
    ticksAlive++;
    if (ticksAlive % 5 == 0) {
      render();
      particles();
    }
  }

  private void render() {
    itemDisplay.setInterpolationDelay(0);
    itemDisplay.setInterpolationDuration(5);

    Matrix4f transformation = new Matrix4f();

    transformation.translate(0, 0.5F, 0);
    transformation.rotateY((float) Math.toRadians(ticksAlive) * 2F);
    transformation.translate(0, -0.5F, 0);

    transformation.scale(2.33F);
    itemDisplay.setTransformationMatrix(transformation);
  }

  private void particles() {
    Location center = itemDisplay.getLocation().clone().add(0, -2.6, 0);
    int radiusPoints = 32;
    double radius = 4.5;
    Vector start = new Vector(radius, 0, 0);
    double anglePerPoint = 360D / radiusPoints;
    double randomStartOffset = ThreadLocalRandom.current().nextDouble(anglePerPoint);
    for (int i = 0; i < radiusPoints; i++) {
      Vector current = start.clone();
      current.rotateAroundY(Math.toRadians(anglePerPoint * i) + randomStartOffset);
      Location particleLoc = center.clone().add(current);
      float size = ThreadLocalRandom.current().nextFloat(0.5F, 1.5F);
      particleLoc.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.AQUA, size));
    }
  }

  public void damage(double amount) {
    this.health -= amount;
    if (this.health <= 0) {
      this.health = 0;
      this.itemDisplay.remove();
    }
    this.decorateTextDisplay(this.textDisplay);
  }

  private boolean isDestroyed() {
    return this.health <= 0;
  }

  public Location getCenter() {
    return this.itemDisplay.getWorld().getHighestBlockAt(this.itemDisplay.getLocation()).getLocation();
  }
}
