package cc.dreamcode.platform.javacord.serdes.notice;

import cc.dreamcode.platform.javacord.exception.JavacordPlatformException;
import cc.dreamcode.platform.javacord.serdes.embed.WrappedEmbedBuilder;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
public class Notice {

    private final NoticeType noticeType;
    private final Object value;

    public CompletableFuture<Message> send(@NonNull Messageable messageable) {
        return this.send(messageable, this.value, null);
    }

    public CompletableFuture<Message> send(@NonNull Messageable messageable, Consumer<MessageBuilder> consumer) {
        return this.send(messageable, this.value, consumer);
    }

    public CompletableFuture<Message> send(@NonNull Messageable messageable, @NonNull Map<String, Object> replaceMap) {
        return this.send(messageable, replaceMap, null);
    }

    public CompletableFuture<Message> send(@NonNull Messageable messageable, @NonNull Map<String, Object> replaceMap, Consumer<MessageBuilder> consumer) {
        switch (this.noticeType) {
            case MESSAGE: {
                return this.send(messageable, Notice.apply((String) this.value, replaceMap), consumer);
            }
            case EMBED: {
                return this.send(messageable, Notice.applyEmbedBuilder((WrappedEmbedBuilder) this.value, replaceMap), consumer);
            }
            default:
                throw new JavacordPlatformException("Cannot resolve unknown notice-type: " + this.noticeType);
        }
    }

    private CompletableFuture<Message> send(@NonNull Messageable messageable, @NonNull Object fixedValue, Consumer<MessageBuilder> consumer) {
        switch (this.noticeType) {
            case MESSAGE: {

                MessageBuilder messageBuilder = new MessageBuilder();

                messageBuilder.setContent((String) fixedValue);

                if (consumer != null) {
                    consumer.accept(messageBuilder);
                }

                return messageBuilder.send(messageable);
            }
            case EMBED: {

                WrappedEmbedBuilder wrappedEmbedBuilder = (WrappedEmbedBuilder) fixedValue;

                return messageable.sendMessage(wrappedEmbedBuilder.toEmbedBuilder());
            }
            default:
                throw new JavacordPlatformException("Cannot resolve unknown notice-type: " + this.noticeType);
        }
    }

    public void applyToResponder(@NonNull InteractionImmediateResponseBuilder responder) {
        this.applyToResponder(responder, this.value);
    }

    public void applyToResponder(@NonNull InteractionImmediateResponseBuilder responder, @NonNull Map<String, Object> replaceMap) {
        switch (this.noticeType) {
            case MESSAGE: {
                this.applyToResponder(responder, Notice.apply((String) this.value, replaceMap));
                break;
            }
            case EMBED: {
                this.applyToResponder(responder, Notice.applyEmbedBuilder((WrappedEmbedBuilder) this.value, replaceMap));
                break;
            }
            default:
                throw new JavacordPlatformException("Cannot resolve unknown notice-type: " + this.noticeType);
        }
    }

    private void applyToResponder(@NonNull InteractionImmediateResponseBuilder responder, @NonNull Object fixedValue) {
        switch (this.noticeType) {
            case MESSAGE: {
                responder.setContent((String) fixedValue);
                break;
            }
            case EMBED: {

                WrappedEmbedBuilder wrappedEmbedBuilder = (WrappedEmbedBuilder) fixedValue;

                responder.addEmbed(wrappedEmbedBuilder.toEmbedBuilder());
                break;
            }
            default:
                throw new JavacordPlatformException("Cannot resolve unknown notice-type: " + this.noticeType);
        }
    }

    public static String apply(String text, @NonNull Map<String, Object> replaceMap) {

        CompiledMessage compiledMessage = CompiledMessage.of(text);
        PlaceholderContext placeholderContext = PlaceholderContext.of(compiledMessage);

        placeholderContext.with(replaceMap);

        return placeholderContext.apply();
    }

    public static WrappedEmbedBuilder applyEmbedBuilder(@NonNull WrappedEmbedBuilder from, @NonNull Map<String, Object> replaceMap) {

        WrappedEmbedBuilder fixedEmbedBuilder = new WrappedEmbedBuilder();

        if (from.getTitle() != null) {
            fixedEmbedBuilder.setTitle(Notice.apply(from.getTitle(), replaceMap));
        }

        if (from.getDescription() != null) {
            fixedEmbedBuilder.setDescription(Notice.apply(from.getDescription(), replaceMap));
        }

        if (from.getUrl() != null) {
            fixedEmbedBuilder.setUrl(Notice.apply(from.getUrl(), replaceMap));
        }

        if (from.getFooterText() != null) {
            if (from.getFooterIconUrl() != null) {
                fixedEmbedBuilder.setFooter(
                        Notice.apply(from.getFooterText(), replaceMap),
                        Notice.apply(from.getFooterIconUrl(), replaceMap)
                );
            }
            else {
                fixedEmbedBuilder.setFooter(Notice.apply(from.getFooterText(), replaceMap));
            }
        }

        if (from.getImageUrl() != null) {
            fixedEmbedBuilder.setImage(Notice.apply(from.getImageUrl(), replaceMap));
        }

        if (from.getAuthorName() != null) {
            fixedEmbedBuilder.setAuthor(
                    Notice.apply(from.getAuthorName(), replaceMap),
                    Notice.apply(from.getAuthorUrl(), replaceMap),
                    Notice.apply(from.getAuthorIconUrl(), replaceMap)
            );
        }

        if (from.getColor() != null) {
            fixedEmbedBuilder.setColor(from.getColor());
        }

        if (from.getThumbnailUrl() != null) {
            fixedEmbedBuilder.setThumbnail(Notice.apply(from.getThumbnailUrl(), replaceMap));
        }

        if (!from.getFields().isEmpty()) {
            from.getFields().forEach(wrappedEmbedField -> fixedEmbedBuilder.addField(
                    Notice.apply(wrappedEmbedField.getName(), replaceMap),
                    Notice.apply(wrappedEmbedField.getValue(), replaceMap),
                    wrappedEmbedField.isInline()
            ));
        }

        return fixedEmbedBuilder;
    }

}
