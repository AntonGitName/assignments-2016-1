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
            boolean flag = true;
            for (Class clazz : implClasses) {
                if (dependency.isAssignableFrom(clazz)) {
                    if (!flag) {
                        throw new AmbiguousImplementationException();
                    }
                    flag = false;
                    List<Class> newCallers = new ArrayList<>(callers);
                    newCallers.add(toCreate);
                    dependencySolver.put(dependency,
                            createInstance(createdInstances, clazz, newCallers, implClasses));
                }
            }
            if (flag) {
                throw new ImplementationNotFoundException();
            }
        }

        final List<Object> cstrArgs = Arrays.stream(dependencies).map(dependencySolver::get).
                collect(Collectors.toList());
        return constructor.newInstance(cstrArgs.toArray());
    }
}
