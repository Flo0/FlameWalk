package com.gestankbratwurst.flamewalk.util.msg.impl;

import com.gestankbratwurst.flamewalk.util.msg.MessageFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class InfoMessageFormat implements MessageFormat {
  @Override
  public TextComponent decoratePrefix(String prefix) {
    return (TextComponent) MiniMessage.miniMessage().deserialize(prefix)
        .append(Component.text(" >> ").color(NamedTextColor.WHITE));
  }

  @Override
  public TextComponent decorateMessage(String messagePart) {
    return Component.text(messagePart).color(NamedTextColor.GRAY);
  }

  @Override
  public TextComponent decorateObject(Object object) {
    return Component.text(object.toString()).color(TextColor.color(255, 225, 77));
  }
}
