
package util;

import model.annotation.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Repository<T> {

    private final HashMap<String, T> data = new HashMap<>();

    public T save(T entity) {
        try {
            Class<?> type = entity.getClass();

            while (type != null) {
                for (Field field : type.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        field.setAccessible(true);
                        Object id = field.get(entity);

                        if (id == null) {
                            throw new IllegalArgumentException("ID cannot be null");
                        }

                        data.put(String.valueOf(id), entity);
                        return entity;
                    }
                }

                type = type.getSuperclass();
            }

            throw new IllegalArgumentException("No @Id field found");
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to read entity ID", e);
        }
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    public T findById(String id) {
        return data.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public boolean delete(String id) {
        return data.remove(id) != null;
    }
}
