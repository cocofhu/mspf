package org.cocofhu.mspf.util;

import lombok.Getter;

import java.util.Objects;

public class Pair<A,B> {
    @Getter
    protected final A first;
    @Getter
    protected final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Pair<?, ?>
                && Objects.equals(first,((Pair<?, ?>)o).first)
                && Objects.equals(second,((Pair<?, ?>)o).second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
