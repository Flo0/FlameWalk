package com.gestankbratwurst.flamewalk.util.msg.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class ErrorMessageFormat extends InfoMessageFormat {

  @Override
  public TextComponent decoratePrefix(String prefix) {
    return Component.text("Error")
        .color(TextColor.color(230, 0, 0))
        .append(Component.text(" >> ").color(NamedTextColor.WHITE));
  }

  @Override
  public TextComponent decorateMessage(String messagePart) {
    return Component.text(messagePart).color(TextColor.color(255, 102, 102));
  }

}
