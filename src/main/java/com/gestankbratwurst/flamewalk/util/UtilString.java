package com.gestankbratwurst.flamewalk.util;

public class UtilString {

  public static String progressBar(double current, double max, int length, String filledPrefix, String emptyPrefix, String element) {
    return progressBar(current / max, length, filledPrefix, emptyPrefix, element);
  }

  public static String progressBar(double progress, int length, String filledPrefix, String emptyPrefix, String element) {
    progress = Math.max(0, Math.min(1, progress));
    int filled = (int) (progress * length);
    int empty = length - filled;
    return filledPrefix + element.repeat(filled) + emptyPrefix + element.repeat(empty);
  }

}
