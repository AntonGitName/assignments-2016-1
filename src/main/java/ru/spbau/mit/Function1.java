package ru.spbau.mit;

/**
 * @author antonpp
 * @since 23/03/16
 */
public abstract class Function1<R, A> {

    public abstract R apply(A arg);

    public <R2> Function1<R2, A> compose(final Function1<? extends R2, ? super R> g) {
        return new Function1<R2, A>() {
            @Override
            public R2 apply(A arg) {
                return g.apply(Function1.this.apply(arg));
            }
        };
    }
}
