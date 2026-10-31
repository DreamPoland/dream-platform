package cc.dreamcode.platform;

import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.component.ComponentExtension;
import cc.dreamcode.platform.component.ComponentService;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ComponentServiceTest {

    private Injector injector;
    private ComponentService componentService;

    @BeforeEach
    void setUp() {
        this.injector = OkaeriInjector.create();
        this.componentService = new ComponentService(this.injector);
        this.componentService.setDebug(false);
    }

    public static class SampleComponent {
        @Getter
        private final String message = "hello";
    }

    public interface CustomMarker {
        String identify();
    }

    public static class CustomComponent implements CustomMarker {
        @Override
        public String identify() {
            return "custom";
        }
    }

    public static class CustomResolver implements ComponentClassResolver<CustomMarker> {
        @Override
        public boolean isAssignableFrom(@NonNull Class<CustomMarker> type) {
            return CustomMarker.class.isAssignableFrom(type);
        }

        @Override
        public String getComponentName() {
            return "custom-marker";
        }

        @Override
        public Map<String, Object> getMetas(@NonNull CustomMarker customMarker) {
            return Collections.singletonMap("key", "value");
        }

        @Override
        public CustomMarker resolve(@NonNull Injector injector, @NonNull Class<CustomMarker> type) {
            return injector.createInstance(type);
        }
    }

    @Test
    void shouldRegisterAndInjectRawComponent() {
        this.componentService.registerComponent(SampleComponent.class);

        SampleComponent resolved = this.injector.createInstance(SampleComponent.class);
        assertNotNull(resolved);
        assertEquals("hello", resolved.getMessage());
    }

    @Test
    void shouldExecuteConsumerOnRegister() {
        AtomicBoolean consumed = new AtomicBoolean(false);

        this.componentService.registerComponent(SampleComponent.class, component -> {
            assertNotNull(component);
            assertEquals("hello", component.getMessage());
            consumed.set(true);
        });

        assertTrue(consumed.get());
    }

    @Test
    void shouldRegisterAndUseCustomResolver() {
        this.componentService.registerResolver(CustomResolver.class);
        this.componentService.registerComponent(CustomComponent.class);

        CustomComponent customComponent = this.injector.createInstance(CustomComponent.class);
        assertNotNull(customComponent);
        assertEquals("custom", customComponent.identify());
    }

    @Test
    void shouldRegisterExtension() {
        AtomicBoolean extensionExecuted = new AtomicBoolean(false);

        ComponentExtension extension = service -> extensionExecuted.set(true);
        this.componentService.registerExtension(extension);

        assertTrue(extensionExecuted.get());
    }
}
