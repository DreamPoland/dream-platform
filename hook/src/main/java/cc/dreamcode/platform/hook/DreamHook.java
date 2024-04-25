package cc.dreamcode.platform.hook;

public interface DreamHook {

    String getPluginName();

    boolean isPresent();

    interface Initializer {
        void onInit();
    }
}
