package ak4ak.utillib.MenuBuilder;

import ak4ak.utillib.UtilLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager implements Listener {

    private static final Map<UUID, MenuSession> sessions = new HashMap<>();

    public static void open(Player player, MenuSession session) {
        player.closeInventory();
        sessions.put(player.getUniqueId(), session);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        MenuSession session = sessions.get(player.getUniqueId());
        if (session == null){
            return;
        }
        if (!e.getInventory().equals(session.getInventory())) return;


        int slot = e.getSlot();
        MenuBuilder template = session.getTemplate();

        e.setCancelled(session.isCancelled());

        if (template.getActions().containsKey(slot)) {
            template.getActions().get(slot).accept(e);
        }

        if (template.getGeneralClick() != null) {
            template.getGeneralClick().accept(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;

        MenuSession session = sessions.remove(player.getUniqueId());
        if (session == null) return;

        MenuBuilder menuBuilder = session.getTemplate();

        if (menuBuilder.getCloseEvent() != null) {
            menuBuilder.getCloseEvent().accept(e);
        }

        if (session.isReopenOnClose()) {
            Bukkit.getScheduler().runTaskLater(UtilLib.instance, () -> {
                InventoryType openType = player.getOpenInventory().getTopInventory().getType();
                if (player.isOnline() && openType != InventoryType.CHEST){
                    menuBuilder.open(player);
                }
            }, 20L); // Открываем через тик, чтобы не конфликтовало с закрытием
        }
    }

    public static MenuSession get(Player player){
        return sessions.get(player.getUniqueId());
    }
}
