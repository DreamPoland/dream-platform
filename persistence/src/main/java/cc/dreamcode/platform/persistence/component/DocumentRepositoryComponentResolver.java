package cc.dreamcode.platform.persistence.component;

import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.persistence.PersistenceCollection;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.RepositoryDeclaration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class DocumentRepositoryComponentResolver implements ComponentClassResolver<DocumentRepository> {

    private final DreamPlatform platform;
    private final DocumentPersistence documentPersistence;

    @Override
    public boolean isAssignableFrom(@NonNull Class<DocumentRepository> documentRepositoryClass) {
        return DocumentRepository.class.isAssignableFrom(documentRepositoryClass);
    }

    @Override
    public String getComponentName() {
        return "repository";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull DocumentRepository documentRepository) {
        return new HashMap<>();
    }

    @Override
    public DocumentRepository resolve(@NonNull Injector injector, @NonNull Class<DocumentRepository> documentRepositoryClass) {

        PersistenceCollection persistenceCollection = PersistenceCollection.of(documentRepositoryClass);
        this.documentPersistence.registerCollection(persistenceCollection);

        return RepositoryDeclaration.of(documentRepositoryClass)
                .newProxy(
                        this.documentPersistence,
                        persistenceCollection,
                        this.platform.getClass().getClassLoader()
                );
    }
}
