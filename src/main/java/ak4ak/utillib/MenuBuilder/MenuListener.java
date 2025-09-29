package ak4ak.utillib.MenuBuilder;

import ak4ak.utillib.ItemBuilder;
import ak4ak.utillib.UtilLib;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.function.Consumer;

public class MenuListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void handleInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuBuilder menu = MenuBuilder.playerMenuMap.get(player.getName());


        if (menu == null)
            return;

            event.setCancelled(menu.isCancelled());

        HashMap<Player,Long>clickCooldown = MenuBuilder.clickCooldownMap;

        if (clickCooldown.containsKey(player)) {
            long lastClick = clickCooldown.get(player);
            if (System.currentTimeMillis() - lastClick < menu.getDistantClick()) {
                // Отменяем обработку
                return;
            }
        }
        clickCooldown.put(player, System.currentTimeMillis());

        HashMap<Integer, Consumer<InventoryClickEvent>>slotAction = menu.getSlotActions();
        Consumer<InventoryClickEvent>consumer = slotAction.get(event.getSlot());

        if (consumer != null)
            consumer.accept(event);

        Consumer<InventoryClickEvent> general = menu.getGeneral();

        if (general != null){
            general.accept(event);
        }

    }

    @EventHandler (ignoreCancelled = true)
    public void closeInventory(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        MenuBuilder menu = MenuBuilder.playerMenuMap.get(player.getName());


        if (menu == null) return;

        menu.acceptCloseEvent(event);

        if (menu.isReopen()) {
            Bukkit.getScheduler().runTaskLater(UtilLib.instance, () -> {
                InventoryType openType = player.getOpenInventory().getTopInventory().getType();
                if (player.isOnline() && openType != InventoryType.CHEST){
                    menu.open(player);
                }
            }, 20L); // Открываем через тик, чтобы не конфликтовало с закрытием
        } else {
            MenuBuilder.playerMenuMap.remove(player.getName());
        }
    }

}
