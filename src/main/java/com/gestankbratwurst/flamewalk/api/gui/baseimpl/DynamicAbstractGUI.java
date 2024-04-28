package com.gestankbratwurst.flamewalk.api.gui.baseimpl;


public abstract non-sealed class DynamicAbstractGUI extends AbstractGuiHandler {

  @Override
  public void decorate() {
    this.clearButtons();
    this.setupButtons();
    super.decorate();
  }

  protected abstract void setupButtons();

}
