package cc.dreamcode.platform.other.component.method;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.CommandContext;
import cc.dreamcode.command.CommandMeta;
import cc.dreamcode.command.CommandPathMeta;
import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.platform.component.ComponentMethodResolver;
import cc.dreamcode.platform.other.component.annotation.SingleCommand;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class CommandMethodResolver implements ComponentMethodResolver<SingleCommand> {

    private final CommandProvider commandProvider;

    @Inject
    public CommandMethodResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<SingleCommand> type) {
        return SingleCommand.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull SingleCommand singleCommand) {
        return new MapBuilder<String, Object>()
                .put("name", singleCommand.name())
                .put("aliases", StringUtil.join(singleCommand.aliases(), ", "))
                .build();
    }

    @Override
    public void apply(@NonNull Injector injector, @NonNull SingleCommand singleCommand, @NonNull Method method, @NonNull Object instance) {

        final CommandBase commandBase = new CommandBase() {
            @Override
            public List<CommandPathMeta> getCommandPaths(@NonNull CommandMeta commandMeta) {
                method.setAccessible(true);

                final Executor executor = method.getAnnotation(Executor.class);
                if (executor == null) {
                    throw new RuntimeException("Executor annotation not found");
                }

                return ListBuilder.of(new CommandPathMeta(commandMeta, method, executor));
            }
        };

        final CommandContext commandContext = new CommandContext(singleCommand.name(), singleCommand.aliases(), singleCommand.description());
        this.commandProvider.register(commandContext, commandBase, instance);
    }
}
