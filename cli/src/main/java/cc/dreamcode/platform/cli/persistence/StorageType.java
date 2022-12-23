package cc.dreamcode.platform.cli.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageType {
    FLAT("FLAT"),
    MYSQL("MySQL"),
    H2("H2"),
    REDIS("Redis"),
    MONGO("MongoDB");

    private final String name;
}
