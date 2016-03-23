package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author antonpp
 * @since 23/03/16
 */
public final class Collections {

    private Collections() {
    }

    public static <R, A> Iterable<R> map(Function1<? extends R, ? super A> mapper,
                                         Iterable<? extends A> iterable) {
        final List<R> mapped = new ArrayList<>();
        for (A obj : iterable) {
            mapped.add(mapper.apply(obj));
        }
        return mapped;
    }

    public static <T> Iterable<T> filter(Predicate<? super T> predicate,
                                         Iterable<? extends T> iterable) {
        final List<T> filtered = new ArrayList<>();
        for (T obj : iterable) {
            if (predicate.apply(obj)) {
                filtered.add(obj);
            }
        }
        return filtered;
    }

    public static <T> Iterable<T> takeWhile(Predicate<? super T> predicate,
                                            Iterable<? extends T> iterable) {
        final List<T> filtered = new ArrayList<>();
        for (T obj : iterable) {
            if (predicate.apply(obj)) {
                filtered.add(obj);
            } else {
                break;
            }
        }
        return filtered;
    }

    public static <T> Iterable<T> takeUnless(Predicate<? super T> predicate,
                                             Iterable<? extends T> iterable) {
        return takeWhile(predicate.not(), iterable);
    }

    public static <A, B> B foldr(Function2<? extends B, ? super A, ? super B> op,
                                 B ini, Iterable<? extends A> foldable) {
        return foldr(op, ini, foldable.iterator());
    }

    private static <A, B> B foldr(Function2<? extends B, ? super A, ? super B> op,
                                  B ini, Iterator<? extends A> it) {
        if (!it.hasNext()) {
            return ini;
        } else {
            return op.apply(it.next(), foldr(op, ini, it));
        }
    }

    public static <A, B> A foldl(Function2<? extends A, ? super A, ? super B> op,
                                 A acc, Iterable<? extends B> foldable) {
        for (B b : foldable) {
            acc = op.apply(acc, b);
        }
        return acc;
    }
}
