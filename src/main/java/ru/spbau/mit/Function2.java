package ru.spbau.mit;

/**
 * @author antonpp
 * @since 23/03/16
 */
public abstract class Function2<Arg1, Arg2, R> {
    public abstract R apply(Arg1 arg1, Arg2 arg2);

    public <R2> Function2<Arg1, Arg2, R2> compose(final Function1<? super R, ? extends R2> g) {
        return new Function2<Arg1, Arg2, R2>() {
            @Override
            public R2 apply(Arg1 arg1, Arg2 arg2) {
                return g.apply(Function2.this.apply(arg1, arg2));
            }
        };
    }

    public Function1<Arg2, R> bind1(final Arg1 arg1) {
        return new Function1<Arg2, R>() {
            @Override
            public R apply(Arg2 arg2) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    public Function1<Arg1, R> bind2(final Arg2 arg2) {
        return new Function1<Arg1, R>() {
            @Override
            public R apply(Arg1 arg1) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    public Function1<Arg1, Function1<Arg2, R>> curry() {
        return new Function1<Arg1, Function1<Arg2, R>>() {
            @Override
            public Function1<Arg2, R> apply(final Arg1 arg1) {
                return bind1(arg1);
            }
        };
    }
}
