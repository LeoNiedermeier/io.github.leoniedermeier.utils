package io.github.leoniedermeier.utils.stream;

import static io.github.leoniedermeier.utils.stream.StreamUtils.filterInstances;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.stream.TreeIteratorUtilsTest.Node;

class StreamUtilsTest {

    @Nested
    class FilterInstances {

        @Test
        void matches() {
            List<String> result = Stream.of("x", 1, "y").flatMap(filterInstances(String.class)).collect(toList());
            assertIterableEquals(asList("x", "y"), result);
        }

        @Test
        void no_matches() {
            List<LocalDate> result = Stream.of("x", 1, "y").flatMap(filterInstances(LocalDate.class)).collect(toList());
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class FlattenedTreeForIterableChildren {

        @Test
        void flattenedTree() {
            Node node = getNode();
            List<String> result = StreamUtils.flattenedTreeForIterableChildren(node, Node::getChildren)
                    .map(Node::getData).collect(Collectors.toList());
            assertEquals(Arrays.asList("F", "B", "A", "D", "C", "E", "G", "I", "H"), result);
        }
    }

    class FlattenedTreeForIterableChildrenAlternative {

        @Test
        void flattenedTreeAlternative() {
            Node node = getNode();

            List<String> result = StreamUtils.flattenedTreeForIterableChildrenAlternative(node, Node::getChildren)
                    .map(Node::getData).collect(Collectors.toList());

            assertEquals(Arrays.asList("F", "B", "G", "A", "D", "C", "E", "I", "H"), result);
        }
    }

    @Nested
    class Iterate {
        @Test
        void test() {
            List<String> result = StreamUtils.iterate("1234", s -> s.length() > 1, s -> s.substring(1))
                    .collect(toList());
            assertLinesMatch(Arrays.asList("1234", "234", "34", "4"), result);
        }
    }
    
    
    @Nested
    class Iterate2 {
        @Test
        void test() {
            List<String> result = StreamUtils.iterate2("1234", s -> s.length() > 1, s -> s.substring(1))
                    .collect(toList());
            assertLinesMatch(Arrays.asList("1234", "234", "34", "4"), result);
        }
    }

    private static Node getNode() {
        // like in https://en.wikipedia.org/wiki/Tree_traversal
        Node root = //
                new Node("F", //
                        new Node("B", //
                                new Node("A"), //
                                new Node("D", //
                                        new Node("C"), new Node("E"))), //
                        new Node("G", //
                                new Node("I", //
                                        new Node("H"))));
        return root;
    }
}
