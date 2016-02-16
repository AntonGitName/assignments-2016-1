package ru.spbau.mit;

import java.util.HashSet;
import java.util.Set;

/**
 * @author antonpp
 * @since 16/02/16
 */
public class StringSetImpl implements StringSet {

    private static final int ALPHABET_SIZE = 23;
    private final Set<String> set = new HashSet<>();
    private final Node root = new Node();

    @Override
    public boolean add(String element) {
//        return addX(element, 0, root);
        return set.add(element);
    }

    private boolean addX(String s, int i, Node node) {
        return false;
    }

    @Override
    public boolean contains(String element) {
        return set.contains(element);
    }

    @Override
    public boolean remove(String element) {
        return set.remove(element);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        int n = 0;
        for (String s : set) {
            if (s.startsWith(prefix)) {
                ++n;
            }
        }
        return n;
    }

    private static final class Node {
        private final char[] children = new char[ALPHABET_SIZE * 2];
        private boolean isLeaf;

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean isLeaf) {
            this.isLeaf = isLeaf;
        }
    }
}
