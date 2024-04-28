package com.gestankbratwurst.flamewalk.util.msg;

import com.gestankbratwurst.flamewalk.util.msg.impl.InfoMessageFormat;
import net.kyori.adventure.text.TextComponent;

public interface MessageFormat {

  MessageFormat INFO = new InfoMessageFormat();
  MessageFormat WARNING = new InfoMessageFormat();
  MessageFormat ERROR = new InfoMessageFormat();

  TextComponent decoratePrefix(String prefix);

  TextComponent decorateMessage(String messagePart);

  TextComponent decorateObject(Object object);

}
