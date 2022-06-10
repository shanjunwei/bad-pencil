package com.home.badpencil.pojo.tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 *  jdk 自带Pair在openJdk中不可用
 */
public class Tuple2<T1,T2> implements Serializable {
    private T1 _1;
    private T2 _2;
    public  T1 _1() { return this._1; }
    public T2 _2() { return this._2; }
    public Tuple2(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }
    public static <T1,T2> Tuple2<T1,T2> of( T1 t1,T2 t2) {
        return new Tuple2<>(t1, t2);
    }
    @Override
    public String toString() {
        return "Tuple2{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple2)) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(_1, tuple2._1) &&
                Objects.equals(_2, tuple2._2);
    }
    @Override
    public int hashCode() {
        return Objects.hash(_1, _2);
    }
}
