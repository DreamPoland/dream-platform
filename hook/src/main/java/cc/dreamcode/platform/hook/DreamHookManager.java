package cc.dreamcode.platform.hook;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class DreamHookManager {

    private final DreamPlatform dreamPlatform;
    private final DreamLogger dreamLogger;

    private final List<DreamHook> dreamHookList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public <T extends DreamHook> Optional<T> get(@NonNull Class<T> pluginHookClass) {
        return (Optional<T>) this.dreamHookList
                .stream()
                .filter(hook -> hook.getClass().isAssignableFrom(pluginHookClass))
                .findFirst();
    }

    public void registerHook(@NonNull Class<? extends DreamHook> pluginHookClass) {
        final long time = System.currentTimeMillis();

        final DreamHook dreamHook = this.dreamPlatform.createInstance(pluginHookClass);
        if (!dreamHook.isPresent()) {
            this.dreamLogger.warning(dreamHook.getPluginName() + " not found! Install it to have full support.");
            return;
        }

        this.dreamHookList.add(dreamHook);

        this.dreamLogger.info(
                new DreamLogger.Builder()
                        .type("Added hook")
                        .name(dreamHook.getPluginName())
                        .took(System.currentTimeMillis() - time)
                        .build()
        );
    }

}
