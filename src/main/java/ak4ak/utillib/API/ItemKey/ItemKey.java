package ak4ak.utillib.API.ItemKey;

import ak4ak.utillib.Main.ItemBuilder;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public class ItemKey {

    private Material material;
    private String itemID;
    private boolean isCustomItem = false;


    public ItemKey(Material material) {
        this.itemID = material.name();
        this.material = material;
        this.isCustomItem = false;
    }


    public ItemKey(String itemID) {
        ItemStack itemStack = new ItemBuilder(itemID).build();

        this.itemID = itemID;
        this.material = itemStack.getType();
        this.isCustomItem = true;
    }

    public ItemKey(ItemStack itemStack) {

        CustomStack stack = CustomStack.byItemStack(itemStack);

        if (stack != null){
            this.itemID = stack.getId();
            this.material = itemStack.getType();
            this.isCustomItem = true;
            return;
        }
        this.material = itemStack.getType();
        this.itemID = material.name();
        this.isCustomItem = false;
    }


    public ItemKey(CustomBlock customBlock) {
        if (customBlock != null){
            this.itemID = customBlock.getId();
            this.material = customBlock.getBaseBlockData().getMaterial();
            this.isCustomItem = true;
            return;
        }
    }

    public ItemKey(Block block) {
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);

        if (customBlock != null){
            this.itemID = customBlock.getId();
            this.material = customBlock.getBlock().getType();
            this.isCustomItem = true;
            return;
        }
        this.material = block.getType();
        this.itemID = block.getType().name();
        this.isCustomItem = false;
    }

    public Material getMaterial() {
        return material;
    }

    public String getItemID() {
        return itemID;
    }

    public ItemStack getItemStack(){

        if (!isCustomItem){
            return new ItemStack(material);
        }

        CustomStack customStack =  CustomStack.getInstance(itemID);

        if (customStack == null){
            System.out.println("ID: " + itemID + " не найдена.");
            return null;
        }
        return customStack.getItemStack();
    }

    public boolean isCustomItem() {
        return isCustomItem;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof ItemKey)) return false;
//        ItemKey other = (ItemKey) o;
//
//        return material == other.material &&
//                Objects.equals(itemID, other.itemID);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemKey)) return false;
        ItemKey other = (ItemKey) o;

        if (isCustomItem || other.isCustomItem) {

            return Objects.equals(itemID, other.itemID);
        }
        return material == other.material;
    }


    @Override
    public int hashCode() {
        return Objects.hash(material, itemID);
    }

    @Override
    public String toString() {
        return (itemID != null)
                ? material + " (IA: " + itemID + ")"
                : material.toString();
    }
}
