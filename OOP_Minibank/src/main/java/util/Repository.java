package util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import model.annotation.Id;

/** Generic in-memory repository. Entity keys are discovered through @Id and reflection. */
public class Repository<T> {
    private final HashMap<String, T> data = new HashMap<>();
    public synchronized T save(T entity) { Objects.requireNonNull(entity, "Entity cannot be null"); data.put(readId(entity), entity); return entity; }
    @SafeVarargs public final synchronized void saveAll(T... items) { for (T item : items) save(item); }
    public synchronized T findById(String id) { return data.get(id); }
    public synchronized ArrayList<T> findAll() { return new ArrayList<>(data.values()); }
    public synchronized boolean delete(String id) { return data.remove(id) != null; }
    public synchronized int size() { return data.size(); }
    private String readId(T entity) {
        try {
            for (Class<?> type = entity.getClass(); type != null; type = type.getSuperclass()) {
                for (Field field : type.getDeclaredFields()) if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true); Object id = field.get(entity);
                    if (id == null || id.toString().isBlank()) throw new IllegalArgumentException("@Id value cannot be blank.");
                    return id.toString();
                }
            }
        } catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to read @Id field", e); }
        throw new IllegalArgumentException("No @Id field found for " + entity.getClass().getName());
    }
    public static <N extends Number> double average(N[] values) {
        Objects.requireNonNull(values, "Values cannot be null");
        if (values.length == 0) throw new IllegalArgumentException("At least one value is required.");
        double sum = 0; for (N value : values) sum += Objects.requireNonNull(value).doubleValue();
        return sum / values.length;
    }
}
