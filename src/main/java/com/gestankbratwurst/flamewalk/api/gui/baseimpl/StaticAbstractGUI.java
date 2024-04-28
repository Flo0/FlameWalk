package com.gestankbratwurst.flamewalk.api.gui.baseimpl;

import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Set;

public abstract non-sealed class StaticAbstractGUI extends AbstractGuiHandler {

  private static final Set<Class<? extends StaticAbstractGUI>> instanceSafeguard = new HashSet<>();

  public StaticAbstractGUI() {
    Preconditions.checkState(!instanceSafeguard.contains(this.getClass()), "Dont instantiate static GUIs multiple times.");
    instanceSafeguard.add(this.getClass());
  }

  @Override
  public void decorate() {
    this.setupButtons();
    super.decorate();
  }

  protected abstract void setupButtons();

  @Override
  public boolean unregisterOnClose() {
    return false;
  }
}
