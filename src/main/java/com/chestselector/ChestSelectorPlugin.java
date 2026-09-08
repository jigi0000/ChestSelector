package com.chestselector;

import com.chestselector.crafting.CraftingRecipeManager;
import com.chestselector.listener.ChestProtectionListener;
import com.chestselector.listener.PlayerDeathListener;
import com.chestselector.manager.ChestSelectorManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestSelectorPlugin extends JavaPlugin {

    private ChestSelectorManager chestSelectorManager;

    @Override
    public void onEnable() {
        getLogger().info("ChestSelector 플러그인이 ���성화되었습니다!");
        
        // Manager 초기화
        this.chestSelectorManager = new ChestSelectorManager(this);
        
        // 리스너 등록
        getServer().getPluginManager().registerEvents(new ChestProtectionListener(chestSelectorManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(chestSelectorManager), this);
        
        // 제작 레시피 등록
        CraftingRecipeManager.registerRecipes(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("ChestSelector 플러그인이 비활성화되었습니다!");
    }

    public ChestSelectorManager getChestSelectorManager() {
        return chestSelectorManager;
    }
}
