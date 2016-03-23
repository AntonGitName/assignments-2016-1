package ru.spbau.mit;

/**
 * @author antonpp
 * @since 23/03/16
 */
public abstract class Function2<R, Arg1, Arg2> {
    public abstract R apply(Arg1 arg1, Arg2 arg2);

    public <R2> Function2<R2, Arg1, Arg2> compose(final Function1<? extends R2, ? super R> g) {
        return new Function2<R2, Arg1, Arg2>() {
            @Override
            public R2 apply(Arg1 arg1, Arg2 arg2) {
                return g.apply(Function2.this.apply(arg1, arg2));
            }
        };
    }

    public Function1<R, Arg2> bind1(final Arg1 arg1) {
        return new Function1<R, Arg2>() {
            @Override
            public R apply(Arg2 arg2) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    public Function1<R, Arg1> bind2(final Arg2 arg2) {
        return new Function1<R, Arg1>() {
            @Override
            public R apply(Arg1 arg1) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    public Function1<Function1<R, Arg2>, Arg1> curry() {
        return new Function1<Function1<R, Arg2>, Arg1>() {
            @Override
            public Function1<R, Arg2> apply(final Arg1 arg1) {
                return bind1(arg1);
            }
        };
    }
}
