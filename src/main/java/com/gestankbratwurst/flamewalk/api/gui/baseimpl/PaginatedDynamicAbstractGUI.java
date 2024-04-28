package com.gestankbratwurst.flamewalk.api.gui.baseimpl;

import com.gestankbratwurst.flamewalk.api.gui.abstraction.GuiButton;
import com.gestankbratwurst.flamewalk.util.ItemBuilder;
import com.gestankbratwurst.flamewalk.util.UtilPlayer;
import lombok.AccessLevel;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class PaginatedDynamicAbstractGUI extends DynamicAbstractGUI {

  protected final List<GuiButton> contentList;
  private final int elementsPerPage;
  private int pageIndex = 0;
  @Setter(AccessLevel.PROTECTED)
  private Function<Integer, Integer> slotSelector = i -> i;

  protected PaginatedDynamicAbstractGUI(int elementsPerPage) {
    this.elementsPerPage = elementsPerPage;
    this.contentList = new ArrayList<>();
  }

  @Override
  public void clearButtons() {
    super.clearButtons();
    contentList.clear();
  }

  @Override
  protected void setupButtons() {
    setupContentList();
    int startIndex = pageIndex * elementsPerPage;
    int endIndex = Math.min((pageIndex + 1) * elementsPerPage, contentList.size());
    List<GuiButton> currentPageButtons = contentList.subList(startIndex, endIndex);
    for (int i = 0; i < currentPageButtons.size(); i++) {
      GuiButton button = currentPageButtons.get(i);
      setButton(slotSelector.apply(i), button);
    }
  }

  protected boolean hasNextPage() {
    int lastPageIndex = (int) Math.ceil(contentList.size() / (double) elementsPerPage) - 1;
    return pageIndex < lastPageIndex;
  }

  protected boolean hasPreviousPage() {
    return pageIndex > 0;
  }

  protected void nextPage(Player viewer) {
    if (hasNextPage()) {
      pageIndex++;
      this.decorate();
      this.renameFor(viewer, getCurrentTitle());
    }
  }

  protected void previousPage(Player viewer) {
    if (hasPreviousPage()) {
      pageIndex--;
      this.decorate();
      this.renameFor(viewer, getCurrentTitle());
    }
  }

  protected GuiButton createNextButton() {
    return GuiButton.builder()
        .eventConsumer(event -> {
          if (this.hasNextPage()) {
            this.nextPage((Player) event.getWhoClicked());
            UtilPlayer.playUIClick((Player) event.getWhoClicked());
          }
        }).iconCreator(() -> {
          String prefix = hasNextPage() ? "§6" : "§7";
          return ItemBuilder.of(Material.GLASS_PANE)
              .name(Component.text(prefix + "Next Page"))
              .build();
        })
        .build();
  }

  protected GuiButton createPreviousButton() {
    return GuiButton.builder()
        .eventConsumer(event -> {
          if (this.hasPreviousPage()) {
            this.previousPage((Player) event.getWhoClicked());
            UtilPlayer.playUIClick((Player) event.getWhoClicked());
          }
        }).iconCreator(() -> {
          String prefix = hasPreviousPage() ? "§6" : "§7";
          return ItemBuilder.of(Material.GLASS_PANE)
              .name(Component.text(prefix + "Previous Page"))
              .build();
        })
        .build();
  }

  protected abstract void setupContentList();

  protected abstract String getCurrentTitle();
}
