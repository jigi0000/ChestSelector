package com.chestselector.listener;

import com.chestselector.manager.ChestSelectorManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class ChestProtectionListener implements Listener {

    private final ChestSelectorManager manager;

    public ChestProtectionListener(ChestSelectorManager manager) {
        this.manager = manager;
    }

    /**
     * 상자 지정기로 상자 지정
     */
    @EventHandler
    public void onChestSelectorUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // 상자 지정기 아이템인지 확인
        if (!isChestSelector(player.getInventory().getItemInMainHand())) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block == null) return;

            // 상자 블록인지 확인
            if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
                manager.selectChest(player, block.getLocation());
                event.setCancelled(true);
            }
        }
    }

    /**
     * 상자 열기 시 약탈 감지
     */
    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        
        Player player = (Player) event.getPlayer();
        InventoryHolder holder = event.getInventory().getHolder();
        
        if (!(holder instanceof org.bukkit.block.Chest)) return;
        
        org.bukkit.block.Chest chest = (org.bukkit.block.Chest) holder;
        Location chestLocation = chest.getLocation();
        
        // 상자의 소유자 확인
        UUID owner = manager.getChestOwner(chestLocation);
        if (owner == null || owner.equals(player.getUniqueId())) {
            return; // 소유자이거나 지정된 상자가 아님
        }
        
        // 약탈 시도 감지
        if (!player.hasPermission("admin")) {
            manager.handleChestRobbery(player, owner);
        }
    }

    /**
     * 상자 지정기 여부 확인
     */
    private boolean isChestSelector(org.bukkit.inventory.ItemStack item) {
        if (item == null || item.getType() != Material.STICK) return false;
        if (item.getItemMeta() == null) return false;
        return item.getItemMeta().getDisplayName().equals("§6상자 지정기");
    }
}
