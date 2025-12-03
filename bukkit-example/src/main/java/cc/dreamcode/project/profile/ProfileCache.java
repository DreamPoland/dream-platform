package cc.dreamcode.project.profile;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileCache {

    private final Map<UUID, Profile> profileMap = new ConcurrentHashMap<>();

    public void add(Profile profile) {
        this.profileMap.put(profile.getUniqueId(), profile);
    }

    public Optional<Profile> findProfile(UUID uniqueId) {
        return Optional.ofNullable(this.profileMap.get(uniqueId));
    }

    public Optional<Profile> findProfileByName(String name) {
        return this.profileMap.values()
                .stream()
                .filter(profile -> profile.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}