package cc.dreamcode.platform.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageType {
    FLAT("FLAT"),
    MYSQL("MySQL"),
    H2("H2"),
    MONGO("MongoDB");

    private final String name;
}
