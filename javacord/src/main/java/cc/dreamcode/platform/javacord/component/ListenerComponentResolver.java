package cc.dreamcode.platform.javacord.component;

import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.javacord.api.DiscordApi;
import org.javacord.api.listener.GloballyAttachableListener;

import java.util.Map;

public class ListenerComponentResolver implements ComponentClassResolver<GloballyAttachableListener> {

    private final DiscordApi discordApi;

    @Inject
    public ListenerComponentResolver(DiscordApi discordApi) {
        this.discordApi = discordApi;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<GloballyAttachableListener> listenerClass) {
        return GloballyAttachableListener.class.isAssignableFrom(listenerClass);
    }

    @Override
    public String getComponentName() {
        return "listener (event)";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull GloballyAttachableListener listener) {
        return new MapBuilder<String, Object>()
                .put("type", listener.getClass().getInterfaces()[0].getSimpleName())
                .build();
    }

    @Override
    public GloballyAttachableListener resolve(@NonNull Injector injector, @NonNull Class<GloballyAttachableListener> listenerClass) {

        final GloballyAttachableListener listener = injector.createInstance(listenerClass);

        this.discordApi.addListener(listener);
        return listener;
    }
}
