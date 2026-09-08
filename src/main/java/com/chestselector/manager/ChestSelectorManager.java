package com.chestselector.manager;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ChestSelectorManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Location> playerSelectedChests = new HashMap<>();
    private final Map<Location, UUID> chestOwners = new HashMap<>();
    private final Map<String, Long> playerPunishments = new HashMap<>(); // "attacker-victim"

    public ChestSelectorManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 플레이어가 상자를 지정합니다
     */
    public void selectChest(Player player, Location chestLocation) {
        playerSelectedChests.put(player.getUniqueId(), chestLocation);
        chestOwners.put(chestLocation, player.getUniqueId());
        player.sendMessage("§a상자가 지정되었습니다: " + chestLocation.getBlockX() + ", " + chestLocation.getBlockY() + ", " + chestLocation.getBlockZ());
    }

    /**
     * 플레이어가 지정한 상자를 반환합니다
     */
    public Location getSelectedChest(Player player) {
        return playerSelectedChests.get(player.getUniqueId());
    }

    /**
     * 상자의 소유자를 반환합니다
     */
    public UUID getChestOwner(Location location) {
        return chestOwners.get(location);
    }

    /**
     * 상자 약탈 시 처리
     */
    public void handleChestRobbery(Player attacker, UUID victimUUID) {
        Player victim = plugin.getServer().getPlayer(victimUUID);
        if (victim == null) return;

        String key = attacker.getUniqueId() + "-" + victimUUID;
        long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // 24시간
        playerPunishments.put(key, expirationTime);

        applyConfinement(attacker, 24 * 60 * 60); // 24시간
        victim.sendMessage("§c" + attacker.getName() + "가 당신의 상자를 약탈했습니다!");
        attacker.sendMessage("§c상자를 약탈했습니다. " + victim.getName() + "에게 에메랄드 64개를 지불할 때까지 구속됩니다.");
    }

    /**
     * 플레이어 사망 시 처리
     */
    public void handlePlayerDeath(Player attacker, UUID victimUUID) {
        Player victim = plugin.getServer().getPlayer(victimUUID);
        if (victim == null) return;

        String key = attacker.getUniqueId() + "-" + victimUUID;
        long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // 24시간
        playerPunishments.put(key, expirationTime);

        applyConfinement(attacker, 24 * 60 * 60); // 24시간
        victim.sendMessage("§c" + attacker.getName() + "가 당신을 죽였습니다!");
        attacker.sendMessage("§c플레이어를 죽였습니다. " + victim.getName() + "에게 에메랄드 32개를 지불할 때까지 구속됩니다.");
    }

    /**
     * 구속 디버프 적용
     */
    public void applyConfinement(Player player, int durationSeconds) {
        PotionEffect confinement = new PotionEffect(
                PotionEffectType.SLOWNESS,
                durationSeconds * 20,
                4,
                false,
                false
        );
        player.addPotionEffect(confinement);
    }

    /**
     * 처벌 상태 확인
     */
    public boolean isPunished(String key) {
        if (!playerPunishments.containsKey(key)) return false;
        
        long expirationTime = playerPunishments.get(key);
        if (System.currentTimeMillis() > expirationTime) {
            playerPunishments.remove(key);
            return false;
        }
        return true;
    }

    /**
     * 처벌 시간 반환 (초 단위)
     */
    public long getPunishmentTimeRemaining(String key) {
        if (!playerPunishments.containsKey(key)) return 0;
        
        long expirationTime = playerPunishments.get(key);
        long remaining = expirationTime - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }
}
