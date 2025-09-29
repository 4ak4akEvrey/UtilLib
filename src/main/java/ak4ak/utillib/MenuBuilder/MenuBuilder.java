package ak4ak.utillib.MenuBuilder;

import ak4ak.utillib.Main.ItemBuilder;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenuBuilder {

    private final String title;
    private final int size;
    private FontImageWrapper fontImageWrapper;

    private boolean reopenOnClose;
    private boolean cancelled;

    private final Map<Integer, Function<Player, ItemStack>> items = new HashMap<>();
    private final Map<Integer, Consumer<InventoryClickEvent>> actions = new HashMap<>();

    private BiConsumer<Player, Inventory> openEvent;
    private Consumer<InventoryCloseEvent> closeEvent;
    private Consumer<InventoryClickEvent> generalClick;

    public MenuBuilder(String title, int size) {
        this.title = title;
        this.size = size;
        this.reopenOnClose = false;
        this.cancelled = true;
    }

    public MenuBuilder(String title, int size, String fontImageWrapper) {
        this.title = "                  " + title;
        this.size = size;
        this.fontImageWrapper = new FontImageWrapper(fontImageWrapper);
        this.reopenOnClose = false;
        this.cancelled = true;
    }

    public MenuBuilder setSlot(int slot, Function<Player, ItemStack> item, Consumer<InventoryClickEvent> action) {
        items.put(slot, item);
        actions.put(slot, action);
        return this;
    }

    public MenuBuilder setSlot(int slot, Function<Player, ItemStack> item) {
        items.put(slot, item);
        return this;
    }

    public MenuBuilder setSlot(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        items.put(slot, player -> item);
        actions.put(slot, action);
        return this;
    }

    public MenuBuilder setSlot(int slot, ItemBuilder item, Consumer<InventoryClickEvent> action) {
        items.put(slot, player -> item.build());
        actions.put(slot, action);
        return this;
    }

    public MenuBuilder setSlot(int slot, ItemStack item) {
        items.put(slot, player -> item);
        return this;
    }

    public MenuBuilder setSlot(int slot, ItemBuilder item) {
        items.put(slot, player -> item.build());
        return this;
    }

    public MenuBuilder fullNull(ItemBuilder builder){

        for (int i = 0; i < size; i++) {

            if (items.get(i) == null){
                setSlot(i, builder);
            }
        }
        return this;
    }

    public MenuBuilder fullNull(ItemStack builder){

        for (int i = 0; i < size; i++) {
            if (items.get(i) == null){
                setSlot(i, builder);
            }
        }
        return this;
    }

    public MenuBuilder fullNull(ItemBuilder builder, Consumer<InventoryClickEvent> eventConsumer){

        for (int i = 0; i < size; i++) {
            if (items.get(i) == null){
                setSlot(i, builder, eventConsumer);
            }
        }
        return this;
    }

    public MenuBuilder fullNull(Function<Player, ItemStack> item, Consumer<InventoryClickEvent> eventConsumer){

        for (int i = 0; i < size; i++) {
            if (items.get(i) == null){
                setSlot(i, item, eventConsumer);
            }
        }
        return this;
    }

    public MenuBuilder fullNull(Function<Player, ItemStack> item){

        for (int i = 0; i < size; i++) {
            if (items.get(i) == null){
                setSlot(i, item);
            }
        }
        return this;
    }

    public void open(Player player){
        new MenuSession(player, this).open();
    }


    public MenuBuilder onOpen(BiConsumer<Player, Inventory> event) {
        this.openEvent = event;
        return this;
    }

    public MenuBuilder onClose(Consumer<InventoryCloseEvent> event) {
        this.closeEvent = event;
        return this;
    }

    public MenuBuilder setGeneral(Consumer<InventoryClickEvent> event) {
        this.generalClick = event;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public Map<Integer, Consumer<InventoryClickEvent>> getActions() {
        return actions;
    }

    public BiConsumer<Player, Inventory> getOpenEvent() {
        return openEvent;
    }

    public Consumer<InventoryCloseEvent> getCloseEvent() {
        return closeEvent;
    }

    public Consumer<InventoryClickEvent> getGeneralClick() {
        return generalClick;
    }

    public Map<Integer, Function<Player, ItemStack>> getItems() {
        return items;
    }


    public MenuBuilder setOpenEvent(BiConsumer<Player, Inventory> openEvent) {
        this.openEvent = openEvent;
        return this;
    }

    public MenuBuilder setCloseEvent(Consumer<InventoryCloseEvent> closeEvent) {
        this.closeEvent = closeEvent;
        return this;
    }

    public MenuBuilder setGeneralClick(Consumer<InventoryClickEvent> generalClick) {
        this.generalClick = generalClick;
        return this;
    }

    public boolean isReopenOnClose() {
        return reopenOnClose;
    }

    public void setReopenOnClose(boolean reopenOnClose) {
        this.reopenOnClose = reopenOnClose;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public FontImageWrapper getFontImageWrapper() {
        return fontImageWrapper;
    }

    public void setFontImageWrapper(FontImageWrapper fontImageWrapper) {
        this.fontImageWrapper = fontImageWrapper;
    }
}

