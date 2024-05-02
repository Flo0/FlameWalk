package com.gestankbratwurst.flamewalk.nms;

import com.gestankbratwurst.flamewalk.game.impl.main.Nexus;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class WalkToNexusGoal extends Goal {
  protected final PathfinderMob mob;
  private final double speedModifier;
  @Nullable
  private Path path;
  private final BlockPos poiPos;
  private final List<BlockPos> visited = Lists.newArrayList();
  private final int distanceToPoi;

  public WalkToNexusGoal(PathfinderMob entity, double speed, int distance, Nexus nexus) {
    Preconditions.checkArgument(nexus != null, "Nexus cannot be null.");
    this.mob = entity;
    this.speedModifier = speed;
    this.distanceToPoi = distance;
    Location center = nexus.getCenter();
    this.poiPos = new BlockPos(center.getBlockX(), center.getBlockY(), center.getBlockZ());
    this.setFlags(EnumSet.of(Flag.MOVE));
    if (!GoalUtils.hasGroundPathNavigation(entity)) {
      throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
    }
  }

  @Override
  public boolean canUse() {
    this.updateVisited();
    // BlockPos blockPos = this.mob.blockPosition();
    Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7, (pos) -> -pos.distSqr(poiPos));

    if (vec3 == null) {
      return false;
    }

    GroundPathNavigation groundPathNavigation = (GroundPathNavigation) this.mob.getNavigation();
    groundPathNavigation.setCanOpenDoors(false);
    this.path = groundPathNavigation.createPath(this.poiPos, 0);

    if (this.path == null) {
      Vec3 vec32 = DefaultRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(this.poiPos), 1.5707963705062866);
      if (vec32 == null) {
        return false;
      }

      groundPathNavigation.setCanOpenDoors(false);
      this.path = this.mob.getNavigation().createPath(vec32.x, vec32.y, vec32.z, 0);
      if (this.path == null) {
        return false;
      }
    }

    for (int i = 0; i < this.path.getNodeCount(); ++i) {
      Node node = this.path.getNode(i);
      BlockPos blockPos2 = new BlockPos(node.x, node.y + 1, node.z);
      if (DoorBlock.isWoodenDoor(this.mob.level(), blockPos2)) {
        this.path = this.mob.getNavigation().createPath(node.x, node.y, node.z, 0);
        break;
      }
    }

    return this.path != null;


  }

  @Override
  public boolean canContinueToUse() {
    if (this.mob.getNavigation().isDone()) {
      return false;
    } else {
      return !this.poiPos.closerToCenterThan(this.mob.position(), this.mob.getBbWidth() + (float) this.distanceToPoi);
    }
  }

  @Override
  public void start() {
    this.mob.getNavigation().moveTo(this.path, this.speedModifier);
  }

  @Override
  public void stop() {
    if (this.mob.getNavigation().isDone() || this.poiPos.closerToCenterThan(this.mob.position(), (double) this.distanceToPoi)) {
      this.visited.add(this.poiPos);
    }
  }

  private void updateVisited() {
    if (this.visited.size() > 15) {
      this.visited.remove(0);
    }
  }
}
