package me.flashyreese.mods.sodiumextra.common.util;

import java.util.LinkedList;

public class EvictingQueue<E> extends LinkedList<E> {
    private int limit;

    public EvictingQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) {
            super.remove();
        }
        return true;
    }
}