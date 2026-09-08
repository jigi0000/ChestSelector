package com.chestselector.crafting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CraftingRecipeManager {

    public static void registerRecipes(JavaPlugin plugin) {
        // 상자 지정기 제작 레시피
        // 2번칸: 유리, 5번칸: 막대기, 8번칸: 막대기
        
        ItemStack chestSelector = createChestSelectorItem();
        NamespacedKey key = new NamespacedKey(plugin, "chest_selector");
        ShapedRecipe recipe = new ShapedRecipe(key, chestSelector);
        
        // 제작대 패턴
        recipe.shape(
                "   ",
                " G ",
                " S "
        );
        
        recipe.setIngredient('G', Material.GLASS);
        recipe.setIngredient('S', Material.STICK);
        
        Bukkit.addRecipe(recipe);
        plugin.getLogger().info("상자 지정기 제작 레시피가 등록되었습니다!");
    }

    public static ItemStack createChestSelectorItem() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6상자 지정기");
            
            List<String> lore = new ArrayList<>();
            lore.add("§7우클릭으로 상자를 지정하세요");
            meta.setLore(lore);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
