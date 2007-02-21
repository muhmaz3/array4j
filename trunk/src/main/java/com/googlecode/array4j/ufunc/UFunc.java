package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.DenseDoubleArray;

public class UFunc {
    public int nargs() {
        return 0;
    }

    public void call(final DenseDoubleArray m1, final DenseDoubleArray m2) {
        UFuncLoop loop = new UFuncLoop(this);

        // TODO can probably put this case inside a function of the loop
//        int loopmeth = 0;
//        switch (loopmeth) {
//        case ONE_UFUNCLOOP:
//            break;
//        case NOBUFFER_UFUNCLOOP:
//            break;
//        case BUFFER_UFUNCLOOP:
//            break;
//        default:
//            throw new AssertionError();
//        }
    }

    public void accumulate() {
    }

    public void reduce() {
    }

    public void reduceat() {
    }

    public void outer() {
    }
}
