package ak4ak.utillib.Main;

import ak4ak.utillib.UtilLib;
import dev.lone.itemsadder.api.ItemsAdder;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemBuilder {
    private ItemStack itemStack;
    private ItemMeta meta;
    public ItemBuilder(Material mat){
        itemStack = new ItemStack(mat);
        meta = itemStack.getItemMeta();
    }
    public ItemBuilder(ItemStack itemStack){

        this.itemStack = new ItemStack(itemStack);
        meta = this.itemStack.getItemMeta();
    }
    public ItemBuilder(String itemId){
        ItemStack itemID = ItemsAdder.getCustomItem(itemId);
        this.itemStack = itemID;
        if (this.itemStack.hasItemMeta()){
            meta = itemStack.getItemMeta();
        }else this.meta = new ItemStack(itemStack).getItemMeta();
    }

    public ItemBuilder(ItemBuilder itemBuilder){

        this.itemStack = new ItemStack(itemBuilder.itemStack);
        meta = this.itemStack.getItemMeta();
    }

    public ItemBuilder setMeta(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    // Новый метод для изменения ItemMeta

    public ItemBuilder setType(Material material){
        this.itemStack.setType(material);

        return this;
    }

    public ItemBuilder amount(int amount){
        itemStack.setAmount(amount);
        return this;
    }
    public ItemBuilder name(String name){
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&f"+name));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder name(Component name) {

        Component safe = Component.empty()
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.WHITE)
                .append(name);

        meta.displayName(safe);
        itemStack.setItemMeta(meta);
        return this;
    }


    public ItemBuilder loreComponent(List<Component> lore){

        List<Component> newLore = new ArrayList<>();

        for (int i = 0; i < lore.size(); i++){
            Component safe = Component.empty()
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.WHITE)
                    .append(lore.get(i));
            newLore.add(safe);
        }
        meta.lore().addAll(newLore);
        itemStack.setItemMeta(meta);
        return this;

    }

    public ItemBuilder addName(String name){
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',meta.getDisplayName()+name));
        itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder modelData(int a){
        meta.setCustomModelData(a);
        itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder hideAtributes(){
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);

        return this;
    }
    public ItemBuilder hidePlacedOn(){
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemStack.setItemMeta(meta);

        return this;
    }



    public ItemBuilder addFlag(ItemFlag itemFlag){
        meta.addItemFlags(itemFlag);
        itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder showFlag(ItemFlag itemFlag){
        if (meta.hasItemFlag(itemFlag))
            meta.removeItemFlags(itemFlag);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        itemStack.setItemMeta(meta);
        return this;
    }


    public ItemBuilder addFlatAttribute(Attribute attribute, double value, EquipmentSlot slot) {
        return addAttribute(attribute, value, AttributeModifier.Operation.ADD_NUMBER, slot);
    }

    /**
     * Добавляет атрибут в формате "+N%" (ADD_SCALAR)
     */
    public ItemBuilder addPercentageAttribute(Attribute attribute, double percentage, EquipmentSlot slot) {
        return addAttribute(attribute, percentage, AttributeModifier.Operation.ADD_SCALAR, slot);
    }

    /**
     * Добавляет атрибут урона (упрощенный метод)
     */
    public ItemBuilder addAttackDamage(double damage) {
        return addFlatAttribute(Attribute.GENERIC_ATTACK_DAMAGE, damage, EquipmentSlot.HAND);
    }

    /**
     * Добавляет атрибут скорости атаки (упрощенный метод)
     */
    public ItemBuilder addAttackSpeed(double speed) {
        return addFlatAttribute(Attribute.GENERIC_ATTACK_SPEED, speed, EquipmentSlot.HAND);
    }

    /**
     * Генерирует лор с атрибутами (можно добавить в описание)
     */
    public List<String> generateAttributeLore() {
        List<String> lore = new ArrayList<>();
        if (!meta.hasAttributeModifiers()) return lore;

        for (Attribute attribute : Attribute.values()) {
            Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(attribute);
            if (modifiers == null || modifiers.isEmpty()) continue;

            for (AttributeModifier modifier : modifiers) {
                String operationSymbol = getOperationSymbol(modifier.getOperation());
                String attributeName = getFriendlyAttributeName(attribute);
                lore.add(ChatColor.GRAY + attributeName + ": " + ChatColor.WHITE +
                        operationSymbol + modifier.getAmount());
            }
        }
        return lore;
    }

    private String getOperationSymbol(AttributeModifier.Operation op) {
        switch (op) {
            case ADD_NUMBER: return "+";
            case ADD_SCALAR: return "+";
            case MULTIPLY_SCALAR_1: return "×";
            default: return "?";
        }
    }

    private String getFriendlyAttributeName(Attribute attribute) {
        return attribute.name().replace("GENERIC_", "").toLowerCase().replace("_", " ");
    }

        public ItemBuilder addAttribute(Attribute attribute, double value, AttributeModifier.Operation operation, EquipmentSlot slot) {
            AttributeModifier modifier = new AttributeModifier(
                    UUID.randomUUID(),
                    attribute.name(),
                    value,
                    operation,
                    slot
            );
            meta.addAttributeModifier(attribute, modifier);
            itemStack.setItemMeta(meta);
            return this;
        }

        /**
         * Удаляет все модификаторы атрибута.
         */
        public ItemBuilder clearAttributes(Attribute attribute) {
            meta.removeAttributeModifier(attribute);
            itemStack.setItemMeta(meta);
            return this;
        }

        /**
         * Быстрый метод для увеличения урона (в процентах).
         * @param percent - процент (например, 15 = +15% урона).
         */
        public ItemBuilder addDamagePercent(double percent) {
            return addAttribute(
                    Attribute.GENERIC_ATTACK_DAMAGE,
                    percent / 100.0,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                    EquipmentSlot.HAND
            );
        }

        /**
         * Быстрый метод для увеличения скорости атаки (в процентах).
         * @param percent - процент (например, -20 = -20% скорости атаки).
         */
        public ItemBuilder addAttackSpeedPercent(double percent) {
            return addAttribute(
                    Attribute.GENERIC_ATTACK_SPEED,
                    percent / 100.0,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                    EquipmentSlot.HAND
            );
        }

    public <T> ItemBuilder setNBT(String key, PersistentDataType<T, T> type, T value) {
        NamespacedKey nbtKey = new NamespacedKey(UtilLib.instance, key);
        meta.getPersistentDataContainer().set(nbtKey, type, value);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Получает NBT-тег (int, String и т. д.).
     * @param defaultValue - значение по умолчанию, если тега нет.
     */
    public <T> T getNBT(String key, PersistentDataType<T, T> type, T defaultValue) {
        NamespacedKey nbtKey = new NamespacedKey(UtilLib.instance, key);
        return meta.getPersistentDataContainer().getOrDefault(nbtKey, type, defaultValue);
    }

    /**
     * Удаляет NBT-тег.
     */
    public ItemBuilder removeNBT(String key) {
        NamespacedKey nbtKey = new NamespacedKey(UtilLib.instance, key);
        meta.getPersistentDataContainer().remove(nbtKey);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Проверяет, есть ли NBT-тег.
     */
    public boolean hasNBT(String key) {
        NamespacedKey nbtKey = new NamespacedKey(UtilLib.instance, key);
        return meta.getPersistentDataContainer().has(nbtKey, PersistentDataType.STRING);
    }




    public ItemBuilder lore(List<String> lore){
        for (int i=0; i < lore.size(); i++){
            lore.set(i,ChatColor.translateAlternateColorCodes('&',"&f"+lore.get(i)));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder lore(String... lore) {
        List<String> listLore = Arrays.asList(lore);
        for (int i=0; i < listLore.size(); i++){
            listLore.set(i,ChatColor.translateAlternateColorCodes('&',"&f"+listLore.get(i)));
        }
        meta.setLore(listLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(Component... lore) {
        List<Component> newLore = new ArrayList<>();

        for (int i = 0; i < lore.length; i++){
            Component safe = Component.empty()
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.WHITE)
                    .append(lore[i]);
            newLore.add(safe);
        }
        meta.lore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeElLore(){
        for (int i = 0; i < meta.getLore().size(); i++) {
            meta.getLore().remove(i);
            itemStack.setItemMeta(meta);
        }
        return this;
    }
    public ItemBuilder removeElLore(int el){
        meta.getLore().remove(el);
        itemStack.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setAttackDamage(double damage) {
        AttributeModifier damageModifier = new AttributeModifier(
                UUID.randomUUID(),
                "generic.attack_damage",
                damage,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.HAND
        );

        // Удалим старые модификаторы, если есть
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLore(int el){
        if (el < meta.getLore().size())
            return this;
        for (int i = 0; i < el; i++) {
            meta.getLore().remove(meta.getLore().size()-i);
        }
        meta.getLore().remove(el);
        itemStack.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setAttackSpeed(double speed) {
        AttributeModifier speedModifier = new AttributeModifier(
                UUID.randomUUID(),
                "generic.attack_speed",
                speed,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.HAND
        );

        // Удалим старый модификатор скорости, если он был
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier);

        itemStack.setItemMeta(meta);
        return this;
    }


    public ItemBuilder addLore(List<String> lore) {
        List<String> currentLore = meta.getLore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();  // Создаем новый список, если lore еще нет
        }
        for (String el : lore) {
            currentLore.add(ChatColor.translateAlternateColorCodes('&', el));
        }
        meta.setLore(currentLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        List<String> currentLore = meta.getLore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();  // Создаем новый список, если lore еще нет
        }
        currentLore.add(ChatColor.translateAlternateColorCodes('&', lore));
        meta.setLore(currentLore);
        itemStack.setItemMeta(meta);
        return this;
    }


    public ItemBuilder addLore(Component lore) {
        List<Component> currentLore = meta.lore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();  // Создаем новый список, если lore еще нет
        }
        Component safe = Component.empty()
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.WHITE)
                .append(lore);

        currentLore.add(safe);
        meta.lore(currentLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder getHead(String name){
        if (Bukkit.getOfflinePlayer(name).isOnline()){
            Player player = Bukkit.getPlayer(name);
        SkullMeta itemMeta  = (SkullMeta) itemStack.getItemMeta();
        itemMeta.setOwningPlayer(player);
        }else{
            OfflinePlayer player = Bukkit.getOfflinePlayer(name);
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
            itemMeta.setOwningPlayer(player);

        }

        return this;

    }
    public ItemBuilder randamount(int amount1, int amount2){
        Random rand = new Random();
        itemStack.setAmount(rand.nextInt(amount1,amount2));
        return this;
    }
    public ItemBuilder onPlacholder(Player player){
        if (itemStack.hasItemMeta()) {
            if (meta.hasLore()){
                meta.setLore(PlaceholderAPI.setPlaceholders(player, meta.getLore()));
            }
            meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, meta.getDisplayName()));
            itemStack.setItemMeta(meta);
        }
        return this;
    }
    public ItemBuilder setPDC(String key, String value){
        meta.getPersistentDataContainer().set(NamespacedKey.fromString(key), PersistentDataType.STRING,value);
        itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder setPDC(String key, Boolean value){
        meta.getPersistentDataContainer().set(NamespacedKey.fromString(key), PersistentDataType.BOOLEAN,value);
        itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder setPDC(String key, int value){
        meta.getPersistentDataContainer().set(NamespacedKey.fromString(key), PersistentDataType.INTEGER,value);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setPlacholder(Player player){
        itemStack.getItemMeta().setLore(PlaceholderAPI.setPlaceholders(player,meta.getLore()));
        return this;
    }



    public ItemStack build(){
        return itemStack;
    }



}
