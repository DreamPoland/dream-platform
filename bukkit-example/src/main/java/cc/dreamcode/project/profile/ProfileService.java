package cc.dreamcode.project.profile;

import cc.dreamcode.platform.DreamLogger;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ProfileService {

    private final DreamLogger logger;        // Injected by Platform
    private final ProfileCache profileCache;       // Injected Component
    private final ProfileRepository profileRepository; // Injected Component

    /**
     * This method runs automatically after dependencies are injected.
     * Use it to load data, start tasks, or register internal handlers.
     */
    @PostConstruct
    public void init() {
        long startTime = System.currentTimeMillis();
        this.logger.info("Loading profiles from database...");

        // Logic: Fetch all profiles from DB -> Register into Cache
        this.profileRepository.findAll()
            .forEach(this.profileCache::add);

        this.logger.info("Loaded profiles in " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    // Find existing profile or create new if not found
    public Profile findOrCreate(@NonNull Player player) {

        final Optional<Profile> profileOptional = this.profileCache.findProfile(player.getUniqueId());

        if (profileOptional.isPresent()) {
            return profileOptional.get();
        }

        final Profile profile = this.profileRepository.findOrCreate(player.getUniqueId(), player.getName()); // Async load recommended in production
        this.profileCache.add(profile);
        return profile;
    }

    // Business logic example
    public void addPoints(@NonNull Profile profile, int amount) {
        profile.setPoints(profile.getPoints() + amount);
        this.profileRepository.save(profile); // Async save recommended in production
    }
}