package ru.spbau.mit;

import java.util.LinkedList;
import java.util.List;

/**
 * @author antonpp
 * @since 16/02/16
 */
public class StringSetImpl implements StringSet {

    private static final int ALPHABET_SIZE = 2 * ('z' - 'a' + 1);
    private final Node root = new Node();

    public static int charToIndex(char c) {
        if (Character.isLowerCase(c)) {
            return c - 'a';
        } else {
            return (c - 'A') + ('z' - 'a') + 1;
        }
    }

    @Override
    public boolean add(String element) {
        return !contains(element) && add(element, root, 0);
    }

    private boolean add(String s, Node node, int i) {
        node.incCounter();
        if (i == s.length()) {
            node.setLeaf(true);
        } else {
            add(s, node.getOrCreateNode(s.charAt(i)), i + 1);
        }
        return true;
    }

    @Override
    public boolean contains(String element) {
        final LinkedList<Node> path = getPath(element);
        return getPath(element) != null && path.getLast().isLeaf();
    }

    private LinkedList<Node> getPath(String s) {
        final LinkedList<Node> path = new LinkedList<>();
        path.add(root);
        Node curNode = root;
        for (int i = 0; i < s.length(); ++i) {
            if (curNode.hasNode(s.charAt(i))) {
                curNode = curNode.getNode(s.charAt(i));
                path.add(curNode);
            } else {
                return null;
            }
        }
        return path;
    }

    @Override
    public boolean remove(String element) {
        final List<Node> path = getPath(element);
        if (path != null) {
            for (Node node : path) {
                node.decCounter();
            }
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return root.getCounter();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        final LinkedList<Node> path = getPath(prefix);
        if (path == null) {
            return 0;
        } else {
            return path.getLast().getCounter();
        }
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
                for (int i = 0; i < children.length; ++i) {
                    children[i] = null;
                }
            }
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean isLeaf) {
            this.isLeaf = isLeaf;
        }

        public Node getOrCreateNode(char c) {
            final int index = charToIndex(c);
            if (children[index] == null) {
                children[index] = new Node();
            }
            return children[index];
        }

        public Node getNode(char c) {
            final int index = charToIndex(c);
            return children[index];
        }

        public boolean hasNode(char c) {
            return children[charToIndex(c)] != null;
        }
    }
}
