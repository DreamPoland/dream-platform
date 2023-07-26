package cc.dreamcode.platform;

import lombok.Data;

@Data(staticConstructor = "create")
public class DreamVersion {

    private final String name;
    private final String version;
    private final String author;

}
