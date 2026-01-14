package com.customspears;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CustomSpears extends JavaPlugin {

    public static Map<String, ItemStack> spears = new HashMap<>();

    @Override
    public void onEnable() {
        registerRecipes();
        getServer().getPluginManager().registerEvents(new SpearListener(), this);
    }

    private void add(String key, String name, String r1, String r2, String r3, Object... data) {
        ItemStack item = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        item.setItemMeta(m);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, key), item);
        recipe.shape(r1, r2, r3);

        for (int i = 0; i < data.length; i += 2)
            recipe.setIngredient((char) data[i], (Material) data[i + 1]);

        Bukkit.addRecipe(recipe);
        spears.put(key, item);
    }

    private void registerRecipes() {

        add("blast_spear","Blast Spear","GTG","CDC","GDG",
                'G',Material.GUNPOWDER,'T',Material.TNT,'C',Material.COAL,'D',Material.DIAMOND_BLOCK);

        add("lightning_spear","Lightning Spear","LCL","INI","LCL",
                'L',Material.LIGHTNING_ROD,'C',Material.COPPER_INGOT,'I',Material.IRON_BLOCK,'N',Material.NETHERITE_INGOT);

        add("glitch_spear","Glitch Spear","DTD","INI","DTD",
                'D',Material.DIAMOND_BLOCK,'T',Material.TOTEM_OF_UNDYING,'I',Material.IRON_BLOCK,'N',Material.NETHER_STAR);

        add("inventory_spear","Inventory Spear","DDD","DND","DDD",
                'D',Material.DIAMOND_BLOCK,'N',Material.NETHERITE_INGOT);

        add("kill_spear","Kill Spear","WSI","CDS","WNS",
                'W',Material.STICK,'S',Material.STONE,'I',Material.IRON_INGOT,
                'C',Material.COPPER_INGOT,'D',Material.DIAMOND,'N',Material.NETHERITE_INGOT);

        add("warden_spear","Warden Spear","DSD","DZD","DND",
                'D',Material.DEEPSLATE,'S',Material.SCULK_CATALYST,
                'Z',Material.ZOMBIE_HEAD,'N',Material.NETHERITE_INGOT);

        add("zombie_spear","Zombie Spear","RCR","DZD","EIE",
                'R',Material.ROTTEN_FLESH,'C',Material.CARROT,
                'D',Material.DIAMOND,'Z',Material.ZOMBIE_HEAD,
                'E',Material.EMERALD_BLOCK,'I',Material.IRON_SWORD);

        add("fire_spear","Fire Spear","FTF","DND","FTF",
                'F',Material.FIRE_CHARGE,'T',Material.TNT,
                'D',Material.DIAMOND,'N',Material.NETHER_STAR);

        add("freeze_spear","Freeze Spear","SIS","PNP","   ",
                'S',Material.SNOWBALL,'I',Material.ICE,
                'P',Material.PACKED_ICE,'N',Material.NETHERITE_INGOT);

        add("poison_spear","Poison Spear","PBP","CTC","PBP",
                'P',Material.PUFFERFISH,'B',Material.BREAD,
                'C',Material.CAKE,'T',Material.TOTEM_OF_UNDYING);

        add("teleport_spear","Teleport Spear","PEP","TET","PEP",
                'P',Material.ENDER_PEARL,'E',Material.ENDER_EYE,
                'T',Material.TOTEM_OF_UNDYING);

        add("wither_spear","Wither Spear","WWW","WSW","WWW",
                'W',Material.WITHER_SKELETON_SKULL,'S',Material.NETHER_STAR);
    }

    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (a.length == 3 && a[0].equalsIgnoreCase("give")) {
            Player p = Bukkit.getPlayer(a[1]);
            if (p != null && spears.containsKey(a[2])) {
                p.getInventory().addItem(spears.get(a[2]));
            }
        }
        return true;
    }
}
