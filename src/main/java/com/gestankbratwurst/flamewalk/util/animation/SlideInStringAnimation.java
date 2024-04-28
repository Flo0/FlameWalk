package com.gestankbratwurst.flamewalk.util.animation;

import lombok.Getter;

import java.util.List;

public class SlideInStringAnimation implements StringAnimation {

  private static final char SLIDE_CHAR = ' ';

  private final List<String> components;
  private final int freeSlideSpaces;
  @Getter
  private final int frameCount;
  private int currentFrameIndex;
  private int fixedComponentCount;
  private int currentSlidingComponentIndex;
  private int currentSlidingComponentSlot;

  public SlideInStringAnimation(List<String> components, int freeSlideSpaces) {
    this.components = components;
    this.freeSlideSpaces = freeSlideSpaces;
    this.frameCount = calculateTotalFrameCount();
    this.currentSlidingComponentSlot = this.freeSlideSpaces;
  }

  @Override
  public String nextFrame() {
    return buildFrame();
  }

  @Override
  public boolean isDone() {
    return this.currentFrameIndex >= this.frameCount;
  }

  @Override
  public void reset() {
    this.currentFrameIndex = 0;
    this.fixedComponentCount = 0;
    this.currentSlidingComponentIndex = 0;
    this.currentSlidingComponentSlot = this.freeSlideSpaces;
  }

  private int calculateTotalFrameCount() {
    int total = 0;
    for (int i = 0; i < this.components.size(); i++) {
      total += this.freeSlideSpaces - i;
    }
    return total + 1;
  }

  private String buildFrame() {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < this.fixedComponentCount; i++) {
      builder.append(this.components.get(i));
    }

    for (int i = fixedComponentCount; i < freeSlideSpaces; i++) {
      if (i == this.currentSlidingComponentSlot) {
        builder.append(this.components.get(this.currentSlidingComponentIndex));
      } else {
        builder.append(SLIDE_CHAR);
      }
    }

    if (currentSlidingComponentSlot == fixedComponentCount) {
      this.currentSlidingComponentIndex++;
      this.fixedComponentCount++;
      this.currentSlidingComponentSlot = this.freeSlideSpaces;
    }

    currentSlidingComponentSlot--;

    this.currentFrameIndex++;
    return builder.toString();
  }

}
