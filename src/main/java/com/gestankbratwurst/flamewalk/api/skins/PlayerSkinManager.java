package com.gestankbratwurst.flamewalk.api.skins;

import org.mineskin.data.Skin;

import java.util.HashMap;

public class PlayerSkinManager {

  private final HashMap<String, Integer> namedSkinIds;
  private final HashMap<Integer, Skin> skinMap;

  public PlayerSkinManager() {
    this.namedSkinIds = new HashMap<>();
    this.skinMap = new HashMap<>();
  }

  public Integer getSkinId(String skinName) {
    return namedSkinIds.get(skinName);
  }

  public Skin getSkin(int skinId) {
    return skinMap.get(skinId);
  }

  public Skin getSkin(String skinName) {
    return getSkin(namedSkinIds.getOrDefault(skinName, 0));
  }

  public void addSkin(Skin skin) {
    namedSkinIds.put(skin.name, skin.id);
    skinMap.put(skin.id, skin);
  }

}
