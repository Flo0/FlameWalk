package com.gestankbratwurst.flamewalk.util.msg;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class Msg {

  public static final char ELEMENT_START = '{';
  public static final char ELEMENT_END = '}';

  @Getter
  private static final String serverPrefix = "<color:#FB150E>F<color:#FB2711>l<color:#FC3914>a<color:#FC4B17>m<color:#FD5D1A>m<color:#FD701D>e<color:#FD821F>n<color:#FE9422>l<color:#FEA625>a<color:#FFB828>u<color:#FFCA2B>f";

  public static void info(CommandSender target, String message, Object... elements) {
    sendFormattedMessage(target, MessageFormat.INFO, message, elements);
  }

  public static void warning(CommandSender target, String message, Object... elements) {
    sendFormattedMessage(target, MessageFormat.WARNING, message, elements);
  }

  public static void error(CommandSender target, String message, Object... elements) {
    sendFormattedMessage(target, MessageFormat.ERROR, message, elements);
  }

  public static void sendFormattedMessage(CommandSender target, MessageFormat format, String message, Object... elements) {
    TextComponent formattedMessage = constructMessage(format, message, elements);
    target.sendMessage(formattedMessage);
  }

  public static TextComponent constructMessage(MessageFormat format, String message, Object... elements) {
    TextComponent component = Component.text("");
    Component prefixComponent = format.decoratePrefix(serverPrefix);
    component = component.append(prefixComponent);

    int elementIndex = 0;
    StringBuilder currentLine = new StringBuilder();

    for (int index = 0; index < message.length(); index++) {
      final char currentChar = message.charAt(index);
      if (index + 1 < message.length() && currentChar == ELEMENT_START && message.charAt(index + 1) == ELEMENT_END) {
        TextComponent messageComponent = format.decorateMessage(currentLine.toString());
        TextComponent objectComponent = format.decorateObject(elements[elementIndex++]);
        component = component.append(messageComponent);
        component = component.append(objectComponent);
        currentLine = new StringBuilder();
        index++;
      } else {
        currentLine.append(currentChar);
      }
    }
    TextComponent messageComponent = format.decorateMessage(currentLine.toString());
    component = component.append(messageComponent);
    return component;
  }

}
