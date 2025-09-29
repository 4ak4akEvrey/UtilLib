package ak4ak.utillib.MenuBuilder;

import ak4ak.utillib.ItemBuilder;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MenuBuilder implements Listener {
    public static HashMap<Player,Long> clickCooldownMap = new HashMap<>();
    public static HashMap<String, MenuBuilder> playerMenuMap = new HashMap<>();

    private Consumer<InventoryCloseEvent> closeEvent;



    private HashMap<Integer, Consumer<InventoryClickEvent>> slotActions;
    private HashMap<Integer,ItemStack>initItemStack;
    private Consumer<InventoryClickEvent> general;
    private FontImageWrapper fontImageWrapper;
    private final String name;
    private final int size;
    private Inventory inventory;
    private boolean reopenOnClose = false;
    private boolean cancelled = true;
    private int distantClick = 0;





    public MenuBuilder(String name, int size, String fontImageWrapper) {
        this.fontImageWrapper = new FontImageWrapper(fontImageWrapper);
        this.name = "                  "+name;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, this.name);
        this.initItemStack = new HashMap<>();
        this.slotActions = new HashMap<>();

        if (clickCooldownMap == null){
            clickCooldownMap = new HashMap<>();
        }
    }
    public MenuBuilder(String name, int size) {
        this.name = name;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, name);
        this.initItemStack = new HashMap<>();
        this.slotActions = new HashMap<>();

        if (clickCooldownMap == null){
            clickCooldownMap = new HashMap<>();
        }
    }


    public MenuBuilder(Inventory inventory) {
        this.name = inventory.getType().name();
        this.size = inventory.getSize();
        this.inventory = inventory;
        this.initItemStack = new HashMap<>();
        this.slotActions = new HashMap<>();

        if (clickCooldownMap == null){
            clickCooldownMap = new HashMap<>();
        }
    }


    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setCloseEvent(Consumer<InventoryCloseEvent> event){
        this.closeEvent = event;
    }


    public Consumer<InventoryClickEvent> getGeneral() {
        return general;
    }


    public void setGeneral(Consumer<InventoryClickEvent> general) {
        this.general = general;
    }

    public boolean isReopen(){
        return reopenOnClose;
    }



    public MenuBuilder setSlot(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> action) {
        inventory.setItem(slot, itemStack);
        slotActions.put(slot, action);
        initItemStack.put(slot,itemStack);
        return this;
    }
    public MenuBuilder setSlot(int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
        initItemStack.put(slot,itemStack);
        return this;
    }
    public static void registerEvent(JavaPlugin plugin){

        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
    }


    public void acceptCloseEvent(InventoryCloseEvent event){
        if (closeEvent != null){
            closeEvent.accept(event);
        }

    }



    public void openNotCrateInventory(Player player) {
        player.closeInventory();



        // Открытие инвентаря
        playerMenuMap.put(player.getName(),this);
        player.openInventory(inventory);




        // Применение текстурного фона, если указан
        if (fontImageWrapper != null) {
            TexturedInventoryWrapper.setPlayerInventoryTexture(player, fontImageWrapper);
        }

    }


    public void open(Player player) {
        player.closeInventory();

        inventory = Bukkit.createInventory(null,size,name);

        // Динамическое обновление предметов с плейсхолдерами для текущего игрока
        for (Map.Entry<Integer, ItemStack> entry : initItemStack.entrySet()) {
            Integer slot = entry.getKey();
            ItemStack item = entry.getValue();
            inventory.setItem(slot, new ItemBuilder(item).onPlacholder(player).build());
        }

        // Открытие инвентаря
        playerMenuMap.put(player.getName(),this);
        player.openInventory(inventory);




        // Применение текстурного фона, если указан
        if (fontImageWrapper != null) {
            TexturedInventoryWrapper.setPlayerInventoryTexture(player, fontImageWrapper);
        }

    }

    public void fullNull(ItemStack itemStack){
        for (int i = 0; i < inventory.getSize(); i++) {

            if (inventory.getItem(i)==null){

                inventory.setItem(i,itemStack);

            }

        }

    }

    public void setReopen(boolean reopen){
        this.reopenOnClose = reopen;
    }

    public HashMap<Integer, Consumer<InventoryClickEvent>> getSlotActions() {
        return slotActions;
    }

    public HashMap<Integer, ItemStack> getInitItemStack() {
        return initItemStack;
    }


    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Inventory getInventory() {
        return inventory;
    }



    public boolean isCancelled() {
        return cancelled;
    }

    public MenuBuilder setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    public int getDistantClick() {
        return distantClick;
    }

    public void setDistantClick(int distantClick) {
        this.distantClick = distantClick;
    }
}