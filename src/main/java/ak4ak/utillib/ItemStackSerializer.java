package ak4ak.utillib;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackSerializer {

    // Безопасная сериализация
    public static String toBase64(ItemStack item) {
        if (item == null) return null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
                dataOutput.writeObject(item);
            }

            String base64 = Base64Coder.encodeLines(outputStream.toByteArray());
            return base64.replaceAll("\\r\\n|\\r|\\n", "");

        } catch (IOException e) {
            System.err.println("❌ Error serializing ItemStack to Base64");
            return null;
        }
    }

    // Безопасная десериализация
    public static ItemStack fromBase64(String data) {
        if (data == null || data.isEmpty()) return null;

        try {
            String cleanedData = data.replaceAll("\\s+", "");

            // Фиксим длину если нужно
            if (cleanedData.length() % 4 != 0) {
                cleanedData = cleanedData + "=".repeat((4 - cleanedData.length() % 4) % 4);
            }

            byte[] decoded = Base64Coder.decodeLines(cleanedData);

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(decoded);
                 BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

                return (ItemStack) dataInput.readObject();
            }

        } catch (Exception e) {
            System.err.println("❌ Error deserializing ItemStack from Base64: " + data);
            return null;
        }
    }
}