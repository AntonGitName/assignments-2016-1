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
        final Constructor constructor = rootCls.getDeclaredConstructors()[0];
        final Class[] dependencies = constructor.getParameterTypes();

        final List<Class> implClasses = new ArrayList<>(implementationClassNames.size());
        for (String className : implementationClassNames) {
            implClasses.add(Class.forName(className));
        }

        final Map<Class, Object> dependencySolver = new HashMap<>(dependencies.length);
        final List<Object> instantiated = new ArrayList<>();
        for (Class dependency : dependencies) {
            Object resolver = null;

            for (Class impl: implClasses) {
                if (dependency.isAssignableFrom(impl)) {
                    if (resolver != null) {
                        throw new AmbiguousImplementationException();
                    } else {
                        try {
                            resolver = createInstance(instantiated, impl);
                        } catch (Exception e) {
                            throw new InjectionCycleException();
                        }
                    }
                }
            }
            if (resolver == null) {
                throw new ImplementationNotFoundException();
            }
            dependencySolver.put(dependency, resolver);
        }
        final List<Object> cstrArgs = Arrays.stream(dependencies).map(dependencySolver::get).
                collect(Collectors.toList());
        return constructor.newInstance(cstrArgs.toArray());
    }

    private static Object createInstance(List<Object> createdInstances, Class toCreate)
            throws Exception {
        final Constructor constructor = toCreate.getDeclaredConstructors()[0];
        final Class[] dependencies = constructor.getParameterTypes();
        final Map<Class, Object> dependencySolver = new HashMap<>(dependencies.length);

        for (Object obj : createdInstances) {
            if (toCreate.isInstance(obj)) {
                return obj;
            }
        }

        for (Class dependency : dependencies) {
            boolean needNewInstance = true;
            for (Object obj : createdInstances) {
                if (dependency.isInstance(obj)) {
                    needNewInstance = false;
                    dependencySolver.put(dependency, obj);
                }
            }
            if (needNewInstance) {
                final Object obj = createInstance(createdInstances, dependency);
                createdInstances.add(obj);
                dependencySolver.put(dependency, obj);
            }
        }

        final List<Object> cstrArgs = Arrays.stream(dependencies).map(dependencySolver::get).
                collect(Collectors.toList());
        return constructor.newInstance(cstrArgs.toArray());
    }
}
