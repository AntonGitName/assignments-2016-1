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

        final List<Class> implObjects = new ArrayList<>(implementationClassNames.size());
        for (String className : implementationClassNames) {
            implObjects.add(Class.forName(className));
        }

        final Map<Class, Object> dependencySolver = new HashMap<>(dependencies.length);
        final List<Object> instatiaded = new ArrayList<>();
        for (Class dependency : dependencies) {
            Object resolver = null;
            for (Object instance : instatiaded) {
                if (dependency.isInstance(instance)) {
                    if (resolver != null) {
                        throw new AmbiguousImplementationException();
                    } else {
                        resolver = instance;
                    }
                }
            }
            for (Class impl: implObjects) {
                if (dependency.isAssignableFrom(impl)) {
                    if (resolver != null) {
                        throw new AmbiguousImplementationException();
                    } else {
                        try {
                            resolver = impl.newInstance();
                        } catch (Exception e) {
                            throw new InjectionCycleException();
                        }
                        instatiaded.add(resolver);
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
}
