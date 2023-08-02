package net.exotia.developer.kit.core.exceptions;

public class InjectorIsNotCreated extends RuntimeException {
    public InjectorIsNotCreated() {
        super("Injector source is null, you may have forgotten to use the PluginFactory#useInjector() method");
    }
}
