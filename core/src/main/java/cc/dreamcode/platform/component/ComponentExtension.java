package cc.dreamcode.platform.component;

import lombok.NonNull;

public interface ComponentExtension {

    void register(@NonNull ComponentManager componentManager);
}
