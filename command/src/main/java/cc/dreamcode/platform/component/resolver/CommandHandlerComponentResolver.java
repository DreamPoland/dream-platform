package cc.dreamcode.platform.component.resolver;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.handler.CommandHandler;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class CommandHandlerComponentResolver extends ComponentClassResolver<Class<CommandHandler>> {

    private @Inject DreamCommandImpl dreamCommand;

    @Override
    public boolean isAssignableFrom(@NonNull Class<CommandHandler> commandHandlerClass) {
        return CommandHandler.class.isAssignableFrom(commandHandlerClass);
    }

    @Override
    public String getComponentName() {
        return "command-handler";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<CommandHandler> commandHandlerClass) {
        final CommandHandler commandHandler = injector.createInstance(commandHandlerClass);
        return new MapBuilder<String, Object>()
                .put("handler-type", commandHandler.getHandlerType().name())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<CommandHandler> commandHandlerClass) {
        final CommandHandler commandHandler = injector.createInstance(commandHandlerClass);

        this.dreamCommand.getHandlers().registerHandler(commandHandler);
        return commandHandler;
    }
}
