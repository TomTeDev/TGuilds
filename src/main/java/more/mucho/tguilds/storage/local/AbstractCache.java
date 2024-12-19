package more.mucho.tguilds.storage.local;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class AbstractCache<ID, T> {
    protected final Map<ID, T> cachedMap = new ConcurrentHashMap<>();

    public Optional<T> get(ID id) {
        return Optional.ofNullable(cachedMap.get(id));
    }

    public void add(T object, ID id) {
        cachedMap.put(id, object);
    }

    public void remove(ID id) {
        cachedMap.remove(id);
    }
    public void clearCache() {
        cachedMap.clear();
    }
    public void addAll(Set<T> objects, Function<T, ID> idExtractor) {
        for (T object : objects) {
            ID id = idExtractor.apply(object); // Extract ID from the object
            cachedMap.put(id, object);
        }
    }
}
