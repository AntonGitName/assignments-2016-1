package ru.spbau.mit;

/**
 * @author antonpp
 * @since 23/03/16
 */
public abstract class Function1<A, R> {

    public abstract R apply(A arg);

    public <R2> Function1<A, R2> compose(final Function1<? super R, ? extends R2> g) {
        return new Function1<A, R2>() {
            @Override
            public R2 apply(A arg) {
                return g.apply(Function1.this.apply(arg));
            }
        };
    }
}
