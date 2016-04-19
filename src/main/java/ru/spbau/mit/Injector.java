package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;


public final class Injector {
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        final Class rootCls = Class.forName(rootClassName);
        final List<Class> implClasses = new ArrayList<>(implementationClassNames.size());
        for (String className : implementationClassNames) {
            implClasses.add(Class.forName(className));
        }
        final List<Object> instantiated = new ArrayList<>();
        return createInstance(instantiated, rootCls, new ArrayList<>(), implClasses);
    }

    private static Object createInstance(List<Object> createdInstances, Class toCreate,
                                         List<Class> callers, List<Class> implClasses)
            throws Exception {

        if (callers.contains(toCreate)) {
            throw new InjectionCycleException();
        }

        final Constructor constructor = toCreate.getDeclaredConstructors()[0];
        final Class[] dependencies = constructor.getParameterTypes();
        final Map<Class, Object> dependencySolver = new HashMap<>(dependencies.length);

        for (Object obj : createdInstances) {
            if (toCreate.isInstance(obj)) {
                return obj;
            }
        }

        for (Class dependency : dependencies) {
            for (Class clazz : implClasses) {
                if (dependency.isAssignableFrom(clazz)) {
                    if (dependencySolver.containsKey(dependency)) {
                        throw new AmbiguousImplementationException();
                    }
                    List<Class> newCallers = new ArrayList<>(callers);
                    newCallers.add(toCreate);
                    final Object obj = createInstance(createdInstances, clazz, newCallers, implClasses);
                    dependencySolver.put(dependency, obj);
                    createdInstances.add(obj);
                }
            }
        }

        for (Class dependency : dependencies) {
            if (!dependencySolver.containsKey(dependency)) {
                throw new ImplementationNotFoundException();
            }
        }

        final List<Object> cstrArgs = Arrays.stream(dependencies).map(dependencySolver::get).
                collect(Collectors.toList());
        final Object instance = constructor.newInstance(cstrArgs.toArray());
        createdInstances.add(instance);
        return instance;
    }
}
