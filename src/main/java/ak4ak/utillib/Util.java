package ak4ak.utillib;

import dev.lone.itemsadder.api.CustomStack;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.TileState;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Util {



    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component colorize(String text) {
        // 1. Сначала заменяем hex-цвета на мини-сообщения
        String processed = text.replaceAll("#([A-Fa-f0-9]{6})", "<color:#$1>");

        // 2. Заменяем legacy-цветы (&a, &l) на мини-сообщения
        processed = convertLegacyToMiniMessage(processed);

        // 3. Обрабатываем всё через MiniMessage
        return MINI_MESSAGE.deserialize(processed);
    }

    private static String convertLegacyToMiniMessage(String text) {
        return text
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underline>")
                .replace("&o", "<italic>")
                .replace("&r", "<reset>");
    }

    public static void giveItem(Player player,ItemStack giveItem){
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(giveItem);

        } else {

            Location location = player.getLocation();
            World world = player.getWorld();
            world.dropItemNaturally(location,giveItem);
        }

    }

    // Альтернатива: только hex-цвета + стандартные цвета Bukkit
    public static String legacyColorize(String text) {
        // Обрабатываем hex-цвета (для версий 1.16+)
        if (isHexSupported()) {
            text = text.replaceAll("#([A-Fa-f0-9]{6})",
                    String.valueOf(ChatColor.valueOf("#$1")));
        }

        // Обрабатываем legacy-цвета
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private static boolean isHexSupported() {
        try {
            ChatColor.valueOf("#FFFFFF");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String args(String[]args, int el){
        StringBuilder message = new StringBuilder();
        for (int i = el; i < args.length; i++) {
            message.append(args[i]);
            if (i != args.length - 1) { // Проверяем, что текущий элемент не последний
                message.append(" ");
            }
        }
        return message.toString();
    }
    public static void sendmessage(Player player, String string){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',string));
    }
    public static String textColor(String text){
        return ChatColor.translateAlternateColorCodes('&',text);
    }
    public static void sendtitle(Player player, String text1){
        player.sendTitle(text1," ");
    }
    public static void sendtitle(Player player, String text1,String text2){

        String str1 = ChatColor.translateAlternateColorCodes('&', text1);
        String str2 = ChatColor.translateAlternateColorCodes('&', text2);


        player.sendTitle(str1,str2);
    }

    public static Component textRGB(String text) {
        if (text.startsWith("#") && text.length() > 7) {
            String hexColor = text.substring(0, 7); // #ff3333
            String message = text.substring(7);     // Опасность!

            try {
                TextColor color = TextColor.fromHexString(hexColor);
                return Component.text(message).color(color);
            } catch (IllegalArgumentException e) {
                // Если цвет невалидный, возвращаем обычный текст
                return Component.text(text);
            }
        }
        return Component.text(text);
    }

    public static ItemStack getHead(String name){
        Player player = (Player) Bukkit.getOfflinePlayer(name);
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta  = (SkullMeta) itemStack.getItemMeta();
        itemMeta.setOwningPlayer(player);
        return itemStack;
    }
    public static int random(int a, int b){
        Random random = new Random();
        return random.nextInt(a,b);
    }
    public static double randomDou(double a, double b){
        Random random = new Random();
        return random.nextDouble(a,b);
    }
    public static void sound(Player player, Sound sound){
        player.playSound(player,sound,1.0f,1.0f);
    }
    public static Boolean randomBool(){
        Random random = new Random();
        return random.nextBoolean();
    }
    public static void removePDC(Player player,String cont){
        if (player.getPersistentDataContainer().has(NamespacedKey.fromString(cont)))
            player.getPersistentDataContainer().remove(NamespacedKey.fromString(cont));
    }



    public static List<String> getLore(ItemStack itemStack){
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()){
            return itemStack.getItemMeta().getLore();
        }else return null;
    }
    public static List<String> getLore(CustomStack customStack){
        if (customStack.getItemStack().hasItemMeta() && customStack.getItemStack().getItemMeta().hasLore()){
            return customStack.getItemStack().getItemMeta().getLore();
        }else return null;

    }

    public static void setPDCBool(Player player,String cont,Boolean value){
        player.getPersistentDataContainer().set(NamespacedKey.fromString(cont), PersistentDataType.BOOLEAN, value);
    }
    public static void setPDCBool(Player player,String cont){
        player.getPersistentDataContainer().set(NamespacedKey.fromString(cont), PersistentDataType.BOOLEAN, true);
    }
    public static void setPDCInt(Player player,String cont, int valu){
        player.getPersistentDataContainer().set(NamespacedKey.fromString(cont), PersistentDataType.INTEGER,valu);
    }
    public static void setPDCStr(Player player,String cont,String valu){
        player.getPersistentDataContainer().set(NamespacedKey.fromString(cont), PersistentDataType.STRING,valu);
    }
    public static Boolean isPDC(Player player, String cont){
        if (player.getPersistentDataContainer().has(NamespacedKey.fromString(cont))){
            return true;
        }return false;
    }
    public static void removePDC(ItemStack itemStack,String cont){
        if (itemStack.getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString(cont)))
            itemStack.getItemMeta().getPersistentDataContainer().remove(NamespacedKey.fromString(cont));
    }
    public static void setPDCBool(ItemStack itemStack,String cont,Boolean value){
        itemStack.getItemMeta().getPersistentDataContainer().set(NamespacedKey.fromString(cont), PersistentDataType.BOOLEAN, value);
    }
    public static void setPDCBool(ItemStack itemStack,String cont){
       itemStack.getItemMeta().getPersistentDataContainer().set(NamespacedKey.fromString(cont), PersistentDataType.BOOLEAN, true);
    }
    public static void setPDCInt(ItemStack itemStack,String cont, int valu){
        itemStack.getItemMeta().getPersistentDataContainer().set(NamespacedKey.fromString(cont), PersistentDataType.INTEGER,valu);
    }
    public static void setPDCStr(ItemStack itemStack,String cont,String valu){
        itemStack.getItemMeta().getPersistentDataContainer().set(NamespacedKey.fromString(cont), PersistentDataType.STRING,valu);
    }
    public static Boolean isPDC(ItemStack itemStack, String cont){
        if (itemStack.getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString(cont))){
            return true;
        }return false;
    }
    public static Object getPDC(ItemStack itemStack, String cont, PersistentDataType persistentDataType){
        return itemStack.getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString(cont),persistentDataType);
    }
    public static Object getPDC(Player player, String cont, PersistentDataType persistentDataType){
        return player.getPersistentDataContainer().get(NamespacedKey.fromString(cont),persistentDataType);
    }

    public static Boolean command(Player player, String PDC, String command, String commtext){
        if (isPDC(player, PDC)) {
            player.performCommand(command + " " + commtext);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,3,3);
            removePDC(player, PDC);
            return true;
        }
        return false;
    }
    public static void command(Player player, String command, String commtext) {
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("TFEtowny"),() ->{
            player.performCommand(command + " " + commtext);

        });
    }
    public static void sendActBar(Player player, String text){
        player.sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(ChatColor.translateAlternateColorCodes('&',text)));
    }


    public static String progressBar(int progress, int total) {
        int barLength = 10;
        int filledLength = (int) ((progress / (double) total) * barLength);
        int emptyLength = barLength - filledLength;
        String progressBar = ChatColor.GREEN + "█".repeat(filledLength) +
                ChatColor.GRAY + "█".repeat(emptyLength);

        return ChatColor.WHITE  + progressBar;
    }
    public static String progressBar(int progress, int total, String addition1, String addition2) {
        // Безопасная проверка данных
        if (total <= 0) total = 1; // Избегаем деления на ноль
        if (progress < 0) progress = 0; // Минимум 0
        if (progress > total) progress = total; // Максимум - total

        int barLength = 7; // Длина шкалы
        int filledLength = (int) ((double) progress / total * barLength);
        int emptyLength = barLength - filledLength;

        // Строим прогресс-бар с разными символами
        String progressBar =
                addition1 +
                        "█".repeat(filledLength) + // Заполненные части
                        "░".repeat(emptyLength) + // Пустые части
                        addition2;

        return progressBar;
    }

    public static void sendMess(Player player, String mess){

        sendmessage(player,mess);
        sendActBar(player,mess);

    }

    public static LivingEntity getTargetEntitySoft(Player player, double maxDistance, double tolerance) {
        Location eye = player.getEyeLocation();
        Vector direction = eye.getDirection().normalize();

        LivingEntity nearest = null;
        double nearestDistSq = maxDistance * maxDistance;

        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity.equals(player)) continue; // пропускаем себя
            if (!entity.getWorld().equals(player.getWorld())) continue;

            Vector toEntity = entity.getLocation().add(0, entity.getHeight() / 2, 0).toVector().subtract(eye.toVector());
            double proj = toEntity.dot(direction);

            if (proj < 0 || proj > maxDistance) continue; // позади или слишком далеко

            // перпендикулярное расстояние от линии взгляда до центра сущности
            Vector closestPoint = eye.toVector().add(direction.clone().multiply(proj));
            double distSq = entity.getLocation().add(0, entity.getHeight() / 2, 0).toVector().distanceSquared(closestPoint);

            if (distSq <= tolerance * tolerance) {
                double distToEyeSq = eye.distanceSquared(entity.getLocation().add(0, entity.getHeight() / 2, 0));
                if (distToEyeSq < nearestDistSq) {
                    nearest = entity;
                    nearestDistSq = distToEyeSq;
                }
            }
        }
        return nearest;
    }

    public static ItemStack getMainHand(Player player){
        return player.getInventory().getItemInMainHand();
    }
    public static ItemStack getOFFHand(Player player){
        return player.getInventory().getItemInOffHand();
    }
    public static Boolean isMainHandString(CustomStack customStack, Player player){
        if (Util.getMainHand(player).hasItemMeta() &&
                Compare.loreString(Util.getMainHand(player),customStack.getItemStack())){
            return true;
        }return false;
    }
    public static Boolean isMainHandString(ItemStack itemStack, Player player){
        if (Util.getMainHand(player).hasItemMeta() &&
                Compare.loreString(Util.getMainHand(player),itemStack)){
            return true;
        }return false;
    }
    public static void removeItems(Player player, ItemStack itemToRemove, int amountToRemove) {
        int remaining = amountToRemove;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item != null && similar(item,itemToRemove)) {
                int itemAmount = item.getAmount();

                if (itemAmount <= remaining) {

                    remaining -= itemAmount;
                    inventory.setItem(i, null);
                } else {

                    item.setAmount(itemAmount - remaining);
                    inventory.setItem(i, item);
                    remaining = 0;
                }

                if (remaining <= 0) {
                    break;
                }
            }
        }
    }
    public static void removeItems(Inventory inventory, ItemStack itemToRemove, int amountToRemove) {
        int remaining = amountToRemove;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && similar(item,itemToRemove)) {
                int itemAmount = item.getAmount();

                if (itemAmount <= remaining) {

                    remaining -= itemAmount;
                    inventory.remove(item);
                } else {

                    item.setAmount(itemAmount - remaining);
                    remaining = 0;
                }

                if (remaining <= 0) {
                    break;
                }
            }
        }
    }


    public static boolean similar(ItemStack item1, ItemStack item2){
        if (item1 == null || item2 == null) {
            return false;
        }


        if (item1.getType() != item2.getType()) {
            return false;
        }

        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        if (meta1 == null || meta2 == null) {
            return false;
        }

        if (!meta1.getDisplayName().equals(meta2.getDisplayName())) {
            return false;
        }

        List<String> lore1 = meta1.getLore();
        List<String> lore2 = meta2.getLore();

        if ((lore1 == null && lore2 != null) || (lore1 != null && lore2 == null)) {
            return false;
        }

        if (lore1 != null && lore2 != null && !lore1.equals(lore2)) {
            return false;
        }

        if (meta1.hasCustomModelData() && meta2.hasCustomModelData()) {
            if (meta1.getCustomModelData() != meta2.getCustomModelData()) {
                return false;
            }
        } else if (meta1.hasCustomModelData() || meta2.hasCustomModelData()) {

            return false;
        }

        return true;
    }

    public static boolean consoleCommand(String command){
        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
        return Bukkit.getServer().dispatchCommand(consoleCommandSender,command);
    }
    public static void command(CommandSender commandSender, String command){
        Bukkit.getServer().dispatchCommand(commandSender,command);
    }

    public static void fullInventory(Inventory inventory, ItemStack itemStack){
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null)
                inventory.setItem(i,itemStack);
        }
    }
    public static ItemStack setPlacholder(Player player, ItemStack itemStack){
        if (itemStack == null || player == null) return itemStack;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;

        // Replace placeholders in the display name
        if (meta.hasDisplayName()) {
            String displayName = meta.getDisplayName();
            displayName = PlaceholderAPI.setPlaceholders(player, displayName);
            meta.setDisplayName(displayName);
        }

        // Replace placeholders in the lore
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            if (lore != null) {
                lore = lore.stream()
                        .map(line -> PlaceholderAPI.setPlaceholders(player, line))
                        .collect(Collectors.toList());
                meta.setLore(lore);
            }
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static String toChatColor(Color color) {
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        StringBuilder chatColor = new StringBuilder("&x");

        for (char c : hex.substring(1).toCharArray()) { // Убираем #
            chatColor.append("&").append(c);
        }
        return chatColor.toString();
    }

    // Метод проверки, является ли текст русским
    public static boolean isRussian(String text) {
        return text != null && text.matches("[а-яА-ЯёЁ\\s]+");
    }

    // Метод, который преобразует первую букву и буквы после пробелов в заглавные
    public static String capitalizeRussianText(String text) {
        if (!isRussian(text)) {
            return text; // Возврат без изменений, если текст не русский
        }

        // Разбиваем текст на слова и обрабатываем каждое слово
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                result.append(c);
                capitalizeNext = true; // Следующий символ после пробела будет заглавным
            } else if (capitalizeNext && Character.isLetter(c)) {
                result.append(Character.toUpperCase(c)); // Преобразование в заглавную букву
                capitalizeNext = false;
            } else {
                result.append(c); // Остальные символы добавляются без изменений
            }
        }

        return result.toString();
    }

    public static boolean isContainerEmpty(Container container) {
        for (ItemStack item : container.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                return false;
            }
        }

        return true;
    }
//Ищет любую цель которвя находится на прицеле игрока
    public static LivingEntity getTargetEntity(Player player, double radius) {
        // Устанавливаем дефолтный радиус, если передан некорректный
        double finalRadius = radius > 0 ? radius : 2.0;

        // 1. Проверяем прямое попадание луча (RayTrace)
        RayTraceResult rayResult = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),               // Откуда идет луч (глаза игрока)
                player.getLocation().getDirection(),   // Направление взгляда
                finalRadius,                          // Максимальная дистанция луча
                0.3,                                  // Размер "хитбокса" сущности
                entity -> entity instanceof Player && !entity.equals(player)  // Фильтр: только другие игроки
        );

        // Если луч попал в игрока — возвращаем его
        if (rayResult != null) {
            Entity hitEntity = rayResult.getHitEntity();
            if (hitEntity instanceof LivingEntity) {
                return (LivingEntity) hitEntity;
            }
        }


        // Возвращаем ближайшего игрока (или null, если никого нет)
        return null;
    }

//Ищет любую цель которвя находится на экране игрока, сначала идет те цели которые ближе всего к экрану
    public static Location getTargetAimEntity(Player player, double maxDistance) {
        // Сначала проверим, не смотрит ли игрок на живую сущность
        LivingEntity targetEntity = null;
        double closest = maxDistance;

        for (Entity entity : player.getNearbyEntities(maxDistance, maxDistance, maxDistance)) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (living == player) continue;

            Location eyeLoc = player.getEyeLocation();
            @NotNull Vector toEntity = living.getEyeLocation().toVector().subtract(eyeLoc.toVector());
            double angle = eyeLoc.getDirection().normalize().angle(toEntity.normalize());

            // Угол меньше ~0.2 радиан ≈ 11.5° (в зависимости от точности и дальности)
            if (angle < 0.2 && eyeLoc.distance(living.getEyeLocation()) < closest) {
                closest = eyeLoc.distance(living.getEyeLocation());
                targetEntity = living;
            }
        }

        if (targetEntity != null) {
            return targetEntity.getLocation();
        }

        // Если сущность не найдена — проверим блок
        Block targetBlock = player.getTargetBlockExact((int) maxDistance);
        if (targetBlock != null) {
            return targetBlock.getLocation().add(0.5, 0.5, 0.5); // Центр блока
        }

        // Ничего не найдено
        return null;



    }


    public static LivingEntity getPlayerLookingAt(Player player, double radius, double maxAngleDegrees) {
        Vector playerDirection = player.getEyeLocation().getDirection().normalize();

        for (Entity nearby : player.getNearbyEntities(radius, radius, radius)) {
            if (!(nearby instanceof LivingEntity target)) continue;
            if (target.equals(player)) continue;

            Vector directionToTarget = target.getEyeLocation().toVector().subtract(player.getEyeLocation().toVector()).normalize();
            double angle = playerDirection.angle(directionToTarget);

            if (Math.toDegrees(angle) <= maxAngleDegrees) {
                return target;
            }
        }

        return null;
    }

    public static @Nullable LivingEntity getEntityInCrosshair(Player player, double range) {
        RayTraceResult result = player.rayTraceEntities((int) range);

        if (result != null && result.getHitEntity() instanceof LivingEntity entity) {
            return entity;
        }

        return null;
    }









    public static LivingEntity getTargetEntityAim(Player player, double radius) {
        // 1. Попытка найти цель по направлению взгляда
        RayTraceResult rayResult = player.getWorld().rayTraceEntities(
                player.getEyeLocation(), player.getLocation().getDirection(), radius, 0.3,
                entity -> entity instanceof LivingEntity && !entity.equals(player)
        );

        if (rayResult != null && rayResult.getHitEntity() instanceof LivingEntity target) {
            return target;
        }

        // 2. Если не нашёл — взять ближайшую живую сущность
        List<LivingEntity> candidates = player.getNearbyEntities(radius, radius, radius).stream()
                .filter(e -> e instanceof LivingEntity && !e.equals(player))
                .map(e -> (LivingEntity) e)
                .sorted(Comparator.comparingDouble(e -> e.getLocation().distanceSquared(player.getLocation())))
                .collect(Collectors.toList());

        return candidates.isEmpty() ? null : candidates.get(0);
    }





        /**
         * Проверяет, есть ли у блока PDC (является ли TileState).
         */
        public boolean hasPDC(Block block) {
            return block.getState() instanceof TileState;
        }

        /**
         * Получает PersistentDataContainer блока.
         * Возвращает null, если блок не поддерживает PDC.
         */
        @Nullable
        public PersistentDataContainer getPDC(Block block) {
            if (!hasPDC(block)) return null;
            return ((TileState) block.getState()).getPersistentDataContainer();
        }

        /**
         * Сохраняет значение в PDC блока.
         * @return true, если успешно, false если блок не поддерживает PDC.
         */
        public <T, Z> boolean set(Block block, String key, PersistentDataType<T, Z> type, Z value) {
            PersistentDataContainer pdc = getPDC(block);
            if (pdc == null) return false;

            NamespacedKey namespacedKey = new NamespacedKey(UtilLib.instance, key);
            pdc.set(namespacedKey, type, value);

            // Сохраняем изменения
            ((TileState) block.getState()).update();
            return true;
        }

        /**
         * Читает значение из PDC блока.
         * @return Значение или null, если ключ не найден или блок не поддерживает PDC.
         */
        @Nullable
        public <T, Z> Z get(Block block, String key, PersistentDataType<T, Z> type) {
            PersistentDataContainer pdc = getPDC(block);
            if (pdc == null) return null;

            NamespacedKey namespacedKey = new NamespacedKey(UtilLib.instance, key);
            return pdc.get(namespacedKey, type);
        }

        /**
         * Проверяет, есть ли ключ в PDC блока.
         */
        public boolean has(Block block, String key) {
            PersistentDataContainer pdc = getPDC(block);
            if (pdc == null) return false;

            NamespacedKey namespacedKey = new NamespacedKey(UtilLib.instance, key);
            return pdc.has(namespacedKey);
        }

        /**
         * Удаляет ключ из PDC блока.
         * @return true, если успешно, false если ключа не было или блок не поддерживает PDC.
         */
        public boolean remove(Block block, String key) {
            PersistentDataContainer pdc = getPDC(block);
            if (pdc == null) return false;

            NamespacedKey namespacedKey = new NamespacedKey(UtilLib.instance, key);
            if (!pdc.has(namespacedKey)) return false;

            pdc.remove(namespacedKey);
            ((TileState) block.getState()).update();
            return true;
        }


}
