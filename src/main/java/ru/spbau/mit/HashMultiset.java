package ru.spbau.mit;

import java.util.*;

/**
 * @author antonpp
 * @since 22/03/16
 */
public class HashMultiset<T> implements Set<T>, Multiset<T> {

    private final Map<Integer, LinkedList<T>> data = new LinkedHashMap<>();
    private int sz;

    @Override
    public int count(Object element) {
        if (data.containsKey(element.hashCode())) {
            int i = 0;
            int hc = element.hashCode();
            for (Object o : data.get(hc)) {
                if (o.equals(element)) {
                    ++i;
                }
            }
            return i;
        } else {
            return 0;
        }
    }

    @Override
    public Set<T> elementSet() {
        return this;
    }

    @Override
    public Set<? extends Entry<T>> entrySet() {
        return new EntrySet();
    }

    @Override
    public int size() {
        return sz;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return count(o) != 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new MultiIterator();
    }

    @Override
    public Object[] toArray() {
        final Object[] array = new Object[sz];
        int i = 0;
        for (Object o : this) {
            array[i++] = o;
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] array) {
        if (array.length >= sz) {
            int i = 0;
            for (T o : this) {
                array[i++] = (T1) o;
            }
            return array;
        } else {
            return (T1[]) toArray();
        }
    }

    @Override
    public boolean add(T t) {
        final int hc = t.hashCode();
        if (!data.containsKey(hc)) {
            data.put(hc, new LinkedList<T>());
        }
        ++sz;
        return data.get(hc).add(t);
    }

    @Override
    public boolean remove(Object o) {
        final boolean res = contains(o);
        if (res) {
            final int hc = o.hashCode();
            Iterator<T> it = data.get(hc).iterator();
            while (it.hasNext()) {
                if (it.next().equals(o)) {
                    it.remove();
                    --sz;
                    break;
                }
            }
            if (data.get(hc).isEmpty()) {
                data.remove(hc);
            }
        }
        return res;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean res = false;
        for (T o : c) {
            if (add(o)) {
                res = true;
            }
        }
        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        for (Object o : c) {
            if (remove(o)) {
                res = true;
            }
        }
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        final Multiset<Object> set = new HashMultiset<>();
        set.addAll(c);
        Iterator<T> it = iterator();
        boolean res = false;
        while (it.hasNext()) {
            final T o = it.next();
            if (!set.contains(o)) {
                it.remove();
                res = true;
                --sz;
            }
        }

        return res;
    }

    @Override
    public void clear() {
        final Multiset<Object> set = new HashMultiset<>();
        set.addAll(this);
        removeAll(set);
    }

    private final class MultiIterator implements Iterator<T> {

        private Iterator<LinkedList<T>> setIterator;
        private Iterator<T> listIterator;
        private LinkedList<T> l;

        private MultiIterator() {
            setIterator = data.values().iterator();
            listIterator = null;
        }

        @Override
        public boolean hasNext() {
            if (listIterator == null) {
                return setIterator.hasNext();
            } else {
                return setIterator.hasNext() || listIterator.hasNext();
            }
        }

        @Override
        public T next() {
            if (listIterator == null || !listIterator.hasNext()) {
                l = setIterator.next();
                listIterator = l.iterator();
            }
            return listIterator.next();
        }

        @Override
        public void remove() {
            listIterator.remove();
            if (l.isEmpty()) {
                setIterator.remove();
            }
            --sz;
        }
    }

    private final class EntrySet extends AbstractSet<Multiset.Entry<T>> {

        @Override
        public Iterator<Entry<T>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return data.size();
        }

        private final class TheEntry implements Multiset.Entry<T> {

            private final LinkedList<T> obj;

            private TheEntry(LinkedList<T> obj) {
                this.obj = obj;
            }

            @Override
            public T getElement() {
                return obj.getFirst();
            }

            @Override
            public int getCount() {
                return obj.size();
            }
        }

        private final class EntryIterator implements Iterator<Multiset.Entry<T>> {

            private Iterator<LinkedList<T>> it;
            private LinkedList<T> l;

            private EntryIterator() {
                it = data.values().iterator();
            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry<T> next() {
                l = it.next();
                return new TheEntry(l);
            }

            @Override
            public void remove() {
                sz -= l.size();
                it.remove();
            }
        }
    }
}
