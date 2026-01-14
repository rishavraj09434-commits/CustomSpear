package com.customspears;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class SpearListener implements Listener {

    private final Map<UUID, Long> cooldown = new HashMap<>();
    private static final long COOLDOWN_TIME = 15000; // 15 seconds

    @EventHandler
    public void onSpearUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        // Must crouch + right click
        if (!player.isSneaking()) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;
        if (!item.getItemMeta().hasDisplayName()) return;

        // Cooldown check
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        if (cooldown.containsKey(uuid)) {
            long last = cooldown.get(uuid);
            if (now - last < COOLDOWN_TIME) {
                long left = (COOLDOWN_TIME - (now - last)) / 1000;
                player.sendMessage("§cCooldown: " + left + "s");
                return;
            }
        }

        // Find target player (aim based, long range)
        Player target = null;
        for (Entity e : player.getNearbyEntities(50, 50, 50)) {
            if (e instanceof Player p && player.hasLineOfSight(p)) {
                target = p;
                break;
            }
        }

        if (target == null) {
            player.sendMessage("§cNo target found!");
            return;
        }

        String name = item.getItemMeta().getDisplayName();
        World w = target.getWorld();

        switch (name) {

            case "Blast Spear" -> {
                w.createExplosion(target.getLocation(), 4F, false, false);
            }

            case "Lightning Spear" -> {
                w.strikeLightning(target.getLocation());
            }

            case "Glitch Spear" -> {
                target.addPotionEffect(
                        new PotionEffect(PotionEffectType.NAUSEA, 200, 1));
                target.addPotionEffect(
                        new PotionEffect(PotionEffectType.BLINDNESS, 200, 1));
            }

            case "Inventory Spear" -> {
                List<ItemStack> items =
                        Arrays.asList(target.getInventory().getContents());
                Collections.shuffle(items);
                target.getInventory().setContents(
                        items.toArray(new ItemStack[0]));
            }

            case "Kill Spear" -> {
                target.damage(40.0, player); // 2x damage
            }

            case "Warden Spear" -> {
                Vector v = target.getLocation()
                        .toVector()
                        .subtract(player.getLocation().toVector())
                        .normalize()
                        .multiply(2);
                target.setVelocity(v);
                target.damage(12, player);
                w.playSound(target.getLocation(),
                        Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1);
            }

            case "Zombie Spear" -> {
                for (int i = 0; i < 10; i++) {
                    Zombie z = (Zombie) w.spawnEntity(
                            target.getLocation(), EntityType.ZOMBIE);
                    z.setBaby(true);
                    z.getEquipment().setHelmet(
                            new ItemStack(Material.NETHERITE_HELMET));
                    z.getEquipment().setChestplate(
                            new ItemStack(Material.NETHERITE_CHESTPLATE));
                    z.getEquipment().setLeggings(
                            new ItemStack(Material.NETHERITE_LEGGINGS));
                    z.getEquipment().setBoots(
                            new ItemStack(Material.NETHERITE_BOOTS));
                    z.setTarget(target);
                }
            }

            case "Fire Spear" -> {
                target.setFireTicks(200);
            }

            case "Freeze Spear" -> {
                target.addPotionEffect(
                        new PotionEffect(PotionEffectType.SLOW, 200, 10));
                target.addPotionEffect(
                        new PotionEffect(PotionEffectType.JUMP, 200, 128));
            }

            case "Poison Spear" -> {
                target.addPotionEffect(
                        new PotionEffect(PotionEffectType.POISON, 200, 2));
            }

            case "Teleport Spear" -> {
                player.teleport(target.getLocation());
            }

            case "Wither Spear" -> {
                target.addPotionEffect(
                        new PotionEffect(PotionEffectType.WITHER, 200, 2));
            }

            default -> {
                return;
            }
        }

        cooldown.put(uuid, now);
    }
                      }
