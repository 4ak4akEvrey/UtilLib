package ak4ak.utillib;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ComponentBuilder {

    private Component component;

    // Конструктор с начальным текстом
    public ComponentBuilder(String text) {
        this.component = Component.text(ChatColor.translateAlternateColorCodes('&',text));

    }
    public ComponentBuilder(ChatColor color, String text) {
        this.component = Component.text(text);



    }

    // Добавление текста
    public ComponentBuilder addText(String text) {
        this.component = this.component.append(Component.text(text));
        return this;
    }

    // Установка цвета текста (используя NamedTextColor)
    public ComponentBuilder color(NamedTextColor color) {
        this.component = this.component.color(color);
        return this;
    }

    // Установка цвета текста (кастомный цвет)
    public ComponentBuilder color(TextColor color) {
        this.component = this.component.color(color);
        return this;
    }

    // Добавление форматирования (жирный, курсив и т.д.)
    public ComponentBuilder decorate(TextDecoration decoration) {
        this.component = this.component.decoration(decoration, true);
        return this;
    }

    // Добавление hover-события
    public ComponentBuilder hoverText(String hoverText) {
        this.component = this.component.hoverEvent(HoverEvent.showText(Component.text(hoverText)));
        return this;
    }
    public ComponentBuilder hoverText(Component component) {
        this.component = this.component.hoverEvent(HoverEvent.showText(component));
        return this;
    }

    // Добавление click-события (выполнение команды)
    public ComponentBuilder clickCommand(String command) {
        this.component = this.component.clickEvent(ClickEvent.runCommand(command));
        return this;
    }

    // Добавление click-события (переход по URL)
    public ComponentBuilder clickUrl(String url) {
        this.component = this.component.clickEvent(ClickEvent.openUrl(url));
        return this;
    }

    // Добавление click-события (предварительный ввод команды)
    public ComponentBuilder suggestCommand(String command) {
        this.component = this.component.clickEvent(ClickEvent.suggestCommand(command));
        return this;
    }

    // Установка текста с использованием кода цвета в стиле ChatColor (&a, &b и т.д.)
    public ComponentBuilder color(String textWithColorCodes) {
        this.component = this.component.append(
                LegacyComponentSerializer.legacyAmpersand().deserialize(textWithColorCodes)
        );
        return this;
    }


    public Component build() {
        return this.component;
    }
}




