package cc.dreamcode.platform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DreamVersionTest {

    @Test
    void shouldCreateVersionCorrectly() {
        DreamVersion version = DreamVersion.create("test-app", "2.0.0", "DreamCode");

        assertNotNull(version);
        assertEquals("test-app", version.getName());
        assertEquals("2.0.0", version.getVersion());
        assertEquals("DreamCode", version.getAuthor());
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        DreamVersion version1 = DreamVersion.create("app", "2.0.0", "Author");
        DreamVersion version2 = DreamVersion.create("app", "2.0.0", "Author");
        DreamVersion version3 = DreamVersion.create("other", "1.0.0", "Author");

        assertEquals(version1, version2);
        assertEquals(version1.hashCode(), version2.hashCode());
        assertNotEquals(version1, version3);
    }
}
