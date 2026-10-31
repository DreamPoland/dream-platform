package cc.dreamcode.platform;

import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.exception.PlatformException;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DreamPlatformTest {

    private TestPlatform platform;

    public static class TestPlatform implements DreamPlatform {
        @Getter private final Injector injector = OkaeriInjector.create();
        @Getter private final DreamLogger dreamLogger = new DreamLogger() {
            @Override public void info(String text) {}
            @Override public void debug(String text) {}
            @Override public void warning(String text) {}
            @Override public void error(String text) {}
            @Override public void error(String text, Throwable throwable) {}
        };
        @Getter private final ComponentService componentService = new ComponentService(injector);
        @Getter private final File dataFolder = new File(".");
        @Getter private final DreamVersion dreamVersion = DreamVersion.create("test", "2.0.0", "author");

        @Override public void enable(@NonNull ComponentService componentService) {}
        @Override public void disable() {}
    }

    public static class SampleInjectable {
        public String value = "hello";
    }

    @BeforeEach
    void setUp() {
        this.platform = new TestPlatform();
    }

    @Test
    void shouldRegisterAndRetrieveInjectable() {
        SampleInjectable sample = new SampleInjectable();
        this.platform.registerInjectable(sample);

        Optional<SampleInjectable> inject = this.platform.getInject(SampleInjectable.class);
        assertTrue(inject.isPresent());
        assertEquals("hello", inject.get().value);

        SampleInjectable orNull = this.platform.getInjectOrNull(SampleInjectable.class);
        assertNotNull(orNull);

        SampleInjectable orThrow = this.platform.getInjectOrThrow(SampleInjectable.class);
        assertNotNull(orThrow);
    }

    @Test
    void shouldReturnNullForMissingInjectable() {
        assertNull(this.platform.getInjectOrNull(String.class));
    }

    @Test
    void shouldThrowExceptionForMissingInjectable() {
        assertThrows(PlatformException.class, () -> this.platform.getInjectOrThrow(String.class));
    }

    @Test
    void shouldRegisterNamedInjectable() {
        SampleInjectable sample = new SampleInjectable();
        sample.value = "named";

        this.platform.registerInjectable("custom-name", sample);

        Optional<SampleInjectable> inject = this.platform.getInject("custom-name", SampleInjectable.class);
        assertTrue(inject.isPresent());
        assertEquals("named", inject.get().value);

        assertEquals("named", this.platform.getInjectOrThrow("custom-name", SampleInjectable.class).value);
    }

    @Test
    void shouldCreateInstanceViaPlatform() {
        SampleInjectable instance = this.platform.createInstance(SampleInjectable.class);
        assertNotNull(instance);
        assertEquals("hello", instance.value);
    }
}
