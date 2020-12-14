package io.github.leoniedermeier.utils.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TreeIteratorUtilsTest {

    public static class Node {
        private List<Node> children;

        private String data;

        public Node(String data) {
            this.data = data;
        }

        public Node(String data, Node... children) {
            this.data = data;
            this.children = Arrays.asList(children);
        }
        public List<Node> getChildren() {
            return children;
        }

        public String getData() {
            return data;
        }
        @Override
        public String toString() {
            return data;
        }
    }

    @Nested
    class BreathFirst {

        @Test
        void traversal() {
            // https://en.wikipedia.org/wiki/Tree_traversal#Depth-first_search
            Node root = getNode();
            Iterator<Node> iterator = TreeIteratorUtils.breathFirst(root, Node::getChildren);
            checkResult(iterator, "F", "B", "G", "A", "D", "I", "C", "E", "H");
        }
    }
    
    @Nested
    class BreathFirstVariation {

        @Test
        void traversal() {
            // https://en.wikipedia.org/wiki/Tree_traversal#Depth-first_search
            Node root = getNode();
            Iterator<Node> iterator = new BreathFirstTreeIteratorVariation<Node>(root, Node::getChildren);
            checkResult(iterator, "F", "B", "G", "A", "D", "I", "C", "E", "H");
        }
    }

    @Nested
    class DepthFirstPreOrder {

        @Test
        void traversal() {
            // https://en.wikipedia.org/wiki/Tree_traversal#Pre-order_(NLR)
            Node root = getNode();
            Iterator<Node> iterator = TreeIteratorUtils.depthFirstPreOrder(root, Node::getChildren);
            checkResult(iterator, "F", "B", "A", "D", "C", "E", "G", "I", "H");
        }
    }

    private static void checkResult(Iterator<Node> iterator, String... list) {
        List<String> result = StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false).map(Node::getData)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(list), result);
    }

    private static Node getNode() {
        // like in https://en.wikipedia.org/wiki/Tree_traversal
        Node root = new Node("F", //
                new Node("B", //
                        new Node("A"), new Node("D", //
                                new Node("C"), new Node("E"))), //
                new Node("G", //
                        new Node("I", //
                                new Node("H"))));
        return root;
    }
}
