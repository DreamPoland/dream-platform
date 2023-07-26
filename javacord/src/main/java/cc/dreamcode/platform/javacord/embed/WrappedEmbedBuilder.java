package cc.dreamcode.platform.javacord.embed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class WrappedEmbedBuilder {

    private final EmbedBuilder embedBuilder = new EmbedBuilder();

    @Getter private String title = null;
    @Getter private String description = null;
    @Getter private String url = null;
    @Getter private Instant timestamp = null;
    @Getter private Color color = null;

    @Getter private String footerText = null;
    @Getter private String footerIconUrl = null;

    @Getter private String imageUrl = null;

    @Getter private String authorName = null;
    @Getter private String authorUrl = null;
    @Getter private String authorIconUrl = null;

    @Getter private String thumbnailUrl = null;

    @Getter private final List<WrappedEmbedField> fields = new ArrayList<>();

    /**
     * Sets the title of the embed.
     *
     * @param title The title of the embed.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setTitle(String title) {
        this.embedBuilder.setTitle(title);

        this.title = title;
        return this;
    }

    /**
     * Sets the description of the embed.
     *
     * @param description The description of the embed.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setDescription(String description) {
        this.embedBuilder.setDescription(description);

        this.description = description;
        return this;
    }

    /**
     * Sets the url of the embed.
     *
     * @param url The url of the embed.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setUrl(String url) {
        this.embedBuilder.setUrl(url);

        this.url = url;
        return this;
    }

    /**
     * Sets the current time as timestamp of the embed.
     *
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setTimestampToNow() {
        this.embedBuilder.setTimestampToNow();

        this.timestamp = Instant.now();
        return this;
    }

    /**
     * Sets the timestamp of the embed.
     *
     * @param timestamp The timestamp to set.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setTimestamp(Instant timestamp) {
        this.embedBuilder.setTimestamp(timestamp);

        this.timestamp = timestamp;
        return this;
    }

    /**
     * Sets the color of the embed.
     *
     * @param color The color of the embed.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setColor(Color color) {
        this.embedBuilder.setColor(color);

        this.color = color;
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setFooter(String text) {
        this.embedBuilder.setFooter(text);

        footerText = text;
        footerIconUrl = null;
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param iconUrl The url of the footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setFooter(String text, String iconUrl) {
        this.embedBuilder.setFooter(text, iconUrl);

        footerText = text;
        footerIconUrl = iconUrl;
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param url The url of the image.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setImage(String url) {
        this.embedBuilder.setImage(url);

        imageUrl = url;
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setAuthor(String name) {
        this.embedBuilder.setAuthor(name);

        authorName = name;
        authorUrl = null;
        authorIconUrl = null;
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param iconUrl The url of the author's icon.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setAuthor(String name, String url, String iconUrl) {
        this.embedBuilder.setAuthor(name, url, iconUrl);

        authorName = name;
        authorUrl = url;
        authorIconUrl = iconUrl;
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param url The url of the thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder setThumbnail(String url) {
        this.embedBuilder.setThumbnail(url);

        thumbnailUrl = url;
        return this;
    }

    /**
     * Adds an inline field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder addInlineField(String name, String value) {
        this.embedBuilder.addField(name, value, true);

        this.fields.add(new WrappedEmbedField(name, value, true));
        return this;
    }

    /**
     * Adds a non-inline field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder addField(String name, String value) {
        this.embedBuilder.addField(name, value, false);

        this.fields.add(new WrappedEmbedField(name, value, false));
        return this;
    }

    /**
     * Adds a field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether the field should be inline or not.
     * @return The current instance in order to chain call methods.
     */
    public WrappedEmbedBuilder addField(String name, String value, boolean inline) {
        this.embedBuilder.addField(name, value, inline);

        this.fields.add(new WrappedEmbedField(name, value, inline));
        return this;
    }

    public EmbedBuilder toEmbedBuilder() {
        return this.embedBuilder;
    }

}
