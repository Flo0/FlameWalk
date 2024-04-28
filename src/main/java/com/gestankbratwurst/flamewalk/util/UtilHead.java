package com.gestankbratwurst.flamewalk.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UtilHead {

  private static final String BASE_TEXTURE_URL = "http://textures.minecraft.net/texture/";
  private static final Map<String, ItemStack> HEAD_CACHE = new HashMap<>();

  public static final Supplier<ItemStack> RED_ORB = () -> getHead("edb5ce0d44c3e8138dc2e7e52c2897bb878e1dbc220d70689c7b6b18d3175e0f");

  public static ItemStack getHead(String minecraftUrl) {
    if (HEAD_CACHE.containsKey(minecraftUrl)) {
      return HEAD_CACHE.get(minecraftUrl).clone();
    }

    URL skinUrl;

    try {
      skinUrl = new URL(BASE_TEXTURE_URL + minecraftUrl);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }

    Preconditions.checkNotNull(skinUrl);

    ItemStack head = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
    PlayerProfile profile = Bukkit.createProfile(minecraftUrl);
    PlayerTextures textures = profile.getTextures();
    textures.setSkin(skinUrl);
    profile.setTextures(textures);
    skullMeta.setPlayerProfile(profile);
    head.setItemMeta(skullMeta);

    HEAD_CACHE.put(minecraftUrl, head);
    return head;
  }

  public static ItemStack ofPlayer(Player player) {
    String playerName = player.getName();
    if (HEAD_CACHE.containsKey(playerName)) {
      return HEAD_CACHE.get(playerName).clone();
    }
    ItemStack head = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
    skullMeta.setOwningPlayer(player);
    head.setItemMeta(skullMeta);
    HEAD_CACHE.put(playerName, head);
    return head;
  }

}
