package ru.spbau.mit;

/**
 * @author antonpp
 * @since 16/02/16
 */
public class StringSetImpl implements StringSet {

    public static final int ALPHABET_SIZE = 2 * ('z' - 'a' + 1);
    private final Node root = new Node();

    public static int charToIndex(char c) {
        if (Character.isLowerCase(c)) {
            return c - 'a';
        } else {
            return 'z' - 'a' + c - 'A' + 1;
        }
    }

    @Override
    public boolean add(String element) {
        return addX(element, root, 0);
    }

    private boolean addX(final String s, final Node node, int i) {
        node.incCounter();
        if (i == s.length()) {
            final boolean res = !node.isLeaf();
            node.setLeaf(true);
            return res;
        }
        return addX(s, node.getNode(s.charAt(i)), i + 1);
    }

    @Override
    public boolean contains(String element) {
        return containsX(element, root, 0);
    }

    private boolean containsX(final String s, final Node node, int i) {
        if (i == s.length()) {
            return node.isLeaf();
        }
        final char c = s.charAt(i);
        return node.hasNode(c) && containsX(s, node.getNode(c), i + 1);
    }

    @Override
    public boolean remove(String element) {
        if (contains(element)) {
            removeX(element, root, 0);
            return true;
        }
        return false;
    }

    private void removeX(final String s, final Node node, int i) {
        node.decCounter();
        if (i != s.length()) {
            removeX(s, node.getNode(s.charAt(i)), i + 1);
        }
    }

    @Override
    public int size() {
        return root.getCounter();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return prefixX(prefix, root, 0);
    }

    public int prefixX(final String s, final Node node, int i) {
        if (i == s.length()) {
            return node.getCounter();
        }
        final char c = s.charAt(i);
        if (node.hasNode(c)) {
            return prefixX(s, node.getNode(c), i + 1);
        }
        return 0;
    }

    private static final class Node {
        private final Node[] children = new Node[ALPHABET_SIZE];
        private int counter;
        private boolean isLeaf;

        public int getCounter() {
            return counter;
        }

        public void incCounter() {
            ++counter;
        }

        public void decCounter() {
            --counter;
            if (counter == 0) {
                isLeaf = false;
            }
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean isLeaf) {
            this.isLeaf = isLeaf;
        }

        public Node getNode(char c) {
            final int index = charToIndex(c);
            if (children[index] == null) {
                children[index] = new Node();
            }
            return children[index];
        }

        public boolean hasNode(char c) {
            return children[charToIndex(c)] != null;
        }
    }
}
