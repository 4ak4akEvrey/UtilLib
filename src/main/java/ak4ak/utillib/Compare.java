package ak4ak.utillib;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Compare {
    public static Boolean lore(ItemStack itemStack1, ItemStack itemStack2){
        if (itemStack1.getItemMeta().getLore().equals(itemStack2.getItemMeta().getLore())){
            return true;
        }return false;
    }
    public static Boolean loreString(ItemStack itemStack1, ItemStack itemStack2){
        if (itemStack1.getItemMeta().getLore().get(0).equals(itemStack2.getItemMeta().getLore().get(0))){
            return true;
        }return false;
    }
    public static Boolean loreString(int stringNamber,ItemStack itemStack1, ItemStack itemStack2){
        if (itemStack1.getItemMeta().getLore().get(stringNamber)
                .equals(itemStack2.getItemMeta().getLore().get(stringNamber))){
            return true;
        }return false;
    }

    public static Boolean name(ItemStack itemStack1, ItemStack itemStack2){
        if (itemStack1.getItemMeta().getDisplayName().equals(itemStack2.getItemMeta().getDisplayName())){
            return true;
        }return false;
    }
    public static Boolean list(int stringNamber, List<String> list1,List<String> list2){
        if (list1.get(stringNamber).equals(list1.get(stringNamber))){
            return true;
        } return false;
    }
    public static Boolean list( List<String> list1,List<String> list2){
        if (list1.get(0).equals(list1.get(0))){
            return true;
        } return false;
    }

}
