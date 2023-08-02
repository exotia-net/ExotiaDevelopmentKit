package net.exotia.developer.kit.injector.entity;

public class Injectable<T> {
    private final String name;
    private final T object;
    private final Class<T> type;

    public Injectable(String name, T object, Class<T> type) {
        this.name = name;
        this.object = object;
        this.type = type;
    }

    public static <T> Injectable<T> of(String name, T object, Class<T> type) {
        return new Injectable<>(name, object, type);
    }

    public String getName() {
        return name;
    }

    public T getObject() {
        return object;
    }

    public Class<T> getType() {
        return type;
    }
}