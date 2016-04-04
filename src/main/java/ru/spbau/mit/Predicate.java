package ru.spbau.mit;

/**
 * @author antonpp
 * @since 23/03/16
 */
public abstract class Predicate<A> extends Function1<A, Boolean> {

    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object arg) {
            return true;
        }
    };
    public static final Predicate<Object> ALWAYS_FALSE = ALWAYS_TRUE.not();

    public Predicate<A> or(final Predicate<? super A> other) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return Predicate.this.apply(arg) || other.apply(arg);
            }
        };
    }

    public Predicate<A> and(final Predicate<? super A> other) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return Predicate.this.apply(arg) && other.apply(arg);
            }
        };
    }

    public Predicate<A> not() {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return !Predicate.this.apply(arg);
            }
        };
    }
}
