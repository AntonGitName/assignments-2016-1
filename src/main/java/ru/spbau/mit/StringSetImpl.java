package ru.spbau.mit;

import java.io.*;

/**
 * @author antonpp
 * @since 16/02/16
 */
public class StringSetImpl implements StringSet, StreamSerializable {

    private static final int ALPHABET_SIZE = 2 * ('z' - 'a' + 1);
    private final Node root = new Node();

    private static int charToIndex(char c) {
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
        return contains(element, root, 0);
    }

    private boolean contains(String s, Node node, int i) {
        if (i == s.length()) {
            return node.isLeaf();
        }
        final char c = s.charAt(i);
        return node.hasNode(c) && contains(s, node.getOrCreateNode(c), i + 1);
    }

    @Override
    public boolean remove(String element) {
        if (contains(element)) {
            remove(element, root, 0);
            return true;
        }
        return false;
    }

    private void remove(String s, Node node, int i) {
        node.decCounter();
        if (i != s.length()) {
            remove(s, node.getOrCreateNode(s.charAt(i)), i + 1);
        }
    }

    @Override
    public int size() {
        return root.getCounter();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return prefix(prefix, root, 0);
    }

    private int prefix(String s, Node node, int i) {
        if (i == s.length()) {
            return node.getCounter();
        }
        final char c = s.charAt(i);
        if (node.hasNode(c)) {
            return prefix(s, node.getOrCreateNode(c), i + 1);
        }
        return 0;
    }

    @Override
    public void serialize(OutputStream out) {
        root.serialize(out);
    }

    @Override
    public void deserialize(InputStream in) {
        root.deserialize(in);
    }

    private static final class Node implements StreamSerializable {
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

        public boolean hasNode(char c) {
            return children[charToIndex(c)] != null;
        }

        @Override
        public void serialize(OutputStream out) {
            try (final DataOutputStream dos = new DataOutputStream(out)) {
                dos.writeBoolean(isLeaf);
                dos.writeInt(counter);
                for (Node child : children) {
                    if (child == null) {
                        dos.writeBoolean(false);
                    } else {
                        dos.writeBoolean(true);
                        child.serialize(out);
                    }
                }
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }

        @Override
        public void deserialize(InputStream in) {
            try (final DataInputStream dis = new DataInputStream(in)) {
                isLeaf = dis.readBoolean();
                counter = dis.readInt();
                for (int i = 0; i < children.length; ++i) {
                    if (dis.readBoolean()) {
                        children[i] = new Node();
                        children[i].deserialize(in);
                    }
                }
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }
    }
}
