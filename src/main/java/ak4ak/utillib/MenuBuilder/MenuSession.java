package ak4ak.utillib.MenuBuilder;

import ak4ak.utillib.Main.ItemBuilder;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuSession {

    private final Player player;
    private final MenuBuilder template;
    private final Inventory inventory;

    private boolean reopenOnClose = false;
    private boolean cancelled = true;

    public MenuSession(Player player, MenuBuilder template) {
        this.player = player;
        this.template = template;
        this.inventory = Bukkit.createInventory(
                null,
                template.getSize(),
                template.getTitle()
        );

        reopenOnClose = template.isReopenOnClose();
        cancelled = template.isCancelled();
        build();
    }

    private void build() {
        for (var entry : template.getItems().entrySet()) {
            ItemStack item = entry.getValue().apply(player);
            inventory.setItem(entry.getKey(), new ItemBuilder(item).onPlacholder(player).build());
        }
    }


    public void open() {
        MenuManager.open(player, this);
        player.openInventory(inventory);

        if (template.getFontImageWrapper() != null) {
            TexturedInventoryWrapper.setPlayerInventoryTexture(player, template.getFontImageWrapper());
        }

        if (template.getOpenEvent() != null){
            template.getOpenEvent().accept(player,inventory);
        }

    }

    public Inventory getInventory() {
        return inventory;
    }

    public MenuBuilder getTemplate() {
        return template;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isReopenOnClose() {
        return reopenOnClose;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setReopenOnClose(boolean reopenOnClose) {
        this.reopenOnClose = reopenOnClose;
    }

    public void toggleCancelled(){
        this.cancelled = !cancelled;
    }

    public void toggleReOpen(){
        this.reopenOnClose = !reopenOnClose;
    }

    public Player getPlayer() {
        return player;
    }
}
