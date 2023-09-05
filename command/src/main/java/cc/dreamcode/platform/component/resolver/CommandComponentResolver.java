package cc.dreamcode.platform.component.resolver;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CommandComponentResolver extends ComponentClassResolver<Class<DreamCommandExecutor>> {

    private @Inject DreamCommandImpl dreamCommand;

    @Override
    public boolean isAssignableFrom(@NonNull Class<DreamCommandExecutor> dreamCommandExecutorClass) {
        return DreamCommandExecutor.class.isAssignableFrom(dreamCommandExecutorClass);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<DreamCommandExecutor> dreamCommandExecutorClass) {
        final Command command = dreamCommandExecutorClass.getAnnotation(Command.class);
        if (command == null) {
            return new HashMap<>();
        }

        return new MapBuilder<String, Object>()
                .put("name", command.label())
                .put("description", command.description())
                .put("aliases", Arrays.toString(command.aliases()))
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<DreamCommandExecutor> dreamCommandExecutorClass) {
        final DreamCommandExecutor executor = injector.createInstance(dreamCommandExecutorClass);

        this.dreamCommand.getCommandRegistry().registerCommand(executor);
        return executor;
    }
}
