package com.chestselector.listener;

import com.chestselector.manager.ChestSelectorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final ChestSelectorManager manager;

    public PlayerDeathListener(ChestSelectorManager manager) {
        this.manager = manager;
    }

    /**
     * 플레이어 사망 시 처리
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = victim.getKiller();

        if (attacker == null) return;
        if (attacker.equals(victim)) return; // 자살은 제외

        manager.handlePlayerDeath(attacker, victim.getUniqueId());
    }
}
