package net.exotia.developer.kit.injector;

import net.exotia.developer.kit.injector.entity.Injectable;
import net.exotia.developer.kit.injector.exception.InjectorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Injector {

    <T> Injector registerInjectable(String name, T object, Class<T> type) throws InjectorException;

    List<Injectable<?>> all();

    Stream<Injectable<?>> stream();

    void removeIf(Predicate<Injectable<?>> predicate);

    <T> List<Injectable<T>> allOf(Class<T> type);

    <T> Stream<Injectable<T>> streamInjectableOf(Class<T> type);

    <T> Stream<T> streamOf(Class<T> type);

    @SuppressWarnings("unchecked")
    default <T> Injector registerInjectable(T object) throws InjectorException {
        Class<T> objectClazz = (Class<T>) object.getClass();
        return this.registerInjectable("", object, objectClazz);
    }

    @SuppressWarnings("unchecked")
    default <T> Injector registerInjectable(String name, T object) throws InjectorException {
        Class<T> objectClazz = (Class<T>) object.getClass();
        return this.registerInjectable(name, object, objectClazz);
    }

    @SuppressWarnings("unchecked")
    default <T> Injector registerExclusive(String name, T object) throws InjectorException {
        Class<T> type = (Class<T>) object.getClass();
        this.removeIf(injectable -> name.equals(injectable.getName()) && (type.isAssignableFrom(injectable.getType()) || injectable.getType().isAssignableFrom(type)));
        return this.registerInjectable(name, object, type);
    }

    default <T> Injector registerExclusive(String name, T object, Class<T> type) throws InjectorException {
        this.removeIf(injectable -> name.equals(injectable.getName()) && type.isAssignableFrom(injectable.getType()));
        return this.registerInjectable(name, object, type);
    }

    @SuppressWarnings("unchecked")
    default <T> Optional<? extends Injectable<T>> getInjectable(String name, Class<T> type) {
        Injectable<T> value = this.getInjectableExact(name, type).orElse(null);

        if ((value == null) && !"".equals(name)) {
            return this.getInjectableExact("", type);
        }

        return Optional.ofNullable(value);
    }

    @SuppressWarnings("unchecked")
    default <T> Optional<T> get(String name, Class<T> type) {
        return this.getInjectable(name, type).map(Injectable::getObject);
    }

    default <T> T getOrThrow(String name, Class<T> type) {
        return this.get(name, type).orElseThrow(() -> new InjectorException("no injectable for " + name + " of type " + type));
    }

    <T> Optional<? extends Injectable<T>> getInjectableExact(String name, Class<T> type);

    default <T> Optional<T> getExact(String name, Class<T> type) {
        return this.getInjectableExact(name, type).map(Injectable::getObject);
    }

    default <T> T getExactOrThrow(String name, Class<T> type) {
        return this.getExact(name, type).orElseThrow(() -> new InjectorException("no exact injectable for " + name + " of type " + type));
    }

    <T> T createInstance(Class<T> clazz) throws InjectorException;

    <T> T injectFields(T instance) throws InjectorException;

    <T> T invokePostConstructs(T instance) throws InjectorException;

    Object invoke(Constructor constructor) throws InjectorException;

    Object invoke(Object object, Method method) throws InjectorException;

    Object[] fillParameters(Parameter[] parameters) throws InjectorException;
}
