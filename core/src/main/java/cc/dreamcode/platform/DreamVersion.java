package cc.dreamcode.platform;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DreamVersion {

    private final String name;
    private final String version;
    private final String author;

    public static DreamVersion create(@NonNull String name, @NonNull String version, @NonNull String author) {
        return new DreamVersion(name, version, author);
    }

}
