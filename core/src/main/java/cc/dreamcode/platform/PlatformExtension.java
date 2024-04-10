package cc.dreamcode.platform;

import lombok.NonNull;

public interface PlatformExtension {

    void register(@NonNull DreamPlatform dreamPlatform);
}
