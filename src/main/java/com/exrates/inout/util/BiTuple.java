package com.exrates.inout.util;

/**
 * @author Denis Savin (pilgrimm333@gmail.com)
 */
public class BiTuple<L,R> {

    public final L left;
    public final R right;

    public BiTuple(L left, R right) {
        this.left = left;
        this.right = right;
    }
}