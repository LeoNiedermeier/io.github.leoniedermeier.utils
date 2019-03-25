/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.leoniedermeier.utils.excecption;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Allows the storage and retrieval of contextual information based on
 * label-value pairs for exceptions.
 *
 * @param <T> The {@link ExceptionContext} type subclass
 */
public interface ExceptionContext<T extends ExceptionContext<T>> {

    /**
     * A label-value pair.
     */
    class Entry implements Serializable {

        @SuppressWarnings("squid:S4926")
        private static final long serialVersionUID = -3878722290376314455L;

        private transient String label;
        private transient Object value;

        public Entry(String label, Object value) {
            super();
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public Object getValue() {
            return value;
        }

        private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            label = (String) s.readObject();
            value = s.readObject();
        }

        private void writeObject(java.io.ObjectOutputStream s) throws IOException {
            s.defaultWriteObject();
            s.writeObject(label);
            if (value instanceof Serializable) {
                s.writeObject(value);
            } else {
                try {
                    s.writeObject(">> Stringified Value Object: <<::" + value);
                } catch (Exception e) {
                    s.writeObject(">> Value Object not serializeable <<");
                }
            }
        }
    }

    /**
     * Adds a contextual label-value pair into this context.
     * <p>
     * The pair will be added to the context, independently of an already existing
     * pair with the same label.
     * </p>
     *
     * @param label The label of the item to add, {@code null} not recommended
     * @param value The value of item to add, may be {@code null}
     * @return {@code this}, for method chaining, not {@code null}
     */
    @SuppressWarnings("unchecked")
    default T addContextValue(final String label, final Object value) {
        getContextEntries().add(new Entry(label, value));
        return (T) this;
    }

    /**
     * Retrieves the first available contextual data value associated with the
     * label.
     *
     * @param label The label to get the contextual value for, may be {@code null}
     * @return The first contextual value associated with the label.
     */
    default Optional<Object> findFirstContextValue(final String label) {
        return findFirstContextValue(label, Object.class);
    }

    /**
     * Retrieves the first available contextual data value associated with the label
     * and which are instances of the given class.
     *
     * @param label         The label to get the contextual value for, may be
     *                      {@code null}
     * @param requiredClass The required class of the values.
     * @return The first contextual value associated with the label and class.
     */
    default <R> Optional<R> findFirstContextValue(final String label, Class<R> requiredClass) {
        return getContextValues(label, requiredClass).findFirst();
    }

    /**
     * Retrieves the last available contextual data value associated with the label.
     *
     * @param label The label to get the contextual value for, may be {@code null}
     * @return The first contextual value associated with the label.
     */
    default Optional<Object> findLastContextValue(final String label) {
        return getContextValues(label, Object.class).reduce((first, second) -> second);
    }

    /**
     * Retrieves the last available contextual data value associated with the label
     * and which are instances of the given class.
     *
     * @param label         The label to get the contextual value for, may be
     *                      {@code null}
     * @param requiredClass The required class of the values.
     * @return the first contextual value associated with the label and class.
     */
    default <R> Optional<R> findLastContextValue(final String label, Class<R> requiredClass) {
        return getContextValues(label, requiredClass).reduce((first, second) -> second);
    }

    /**
     * Retrieves the full list of {@link Entry}s defined in the contextual data.
     *
     * @return The list of {@link Entry}s, not {@code null}
     */
    List<Entry> getContextEntries();

    /**
     * Retrieves the full set of labels defined in the contextual data.
     *
     * @return the set of labels, not {@code null}
     */
    default Set<String> getContextLabels() {
        return getContextEntries().stream().map(Entry::getLabel).collect(toSet());
    }

    /**
     * Retrieves all the contextual data values associated with the label.
     *
     * @param label the label to get the contextual values for, may be {@code null}
     * @return the contextual values associated with the label, never {@code null}
     */
    default Stream<Object> getContextValues(final String label) {
        return getContextValues(label, Object.class);

    }

    /**
     * Retrieves all the contextual data values associated with the label and which
     * are instances of the given class.
     *
     * @param label         The label to get the contextual values for, may be
     *                      {@code null}
     * @param requiredClass The required class of the values.
     * @return the contextual values associated with the label, never {@code null}
     */
    default <R> Stream<R> getContextValues(final String label, Class<R> requiredClass) {
        return getContextEntries().stream().filter(e -> Objects.equals(label, e.getLabel())).map(Entry::getValue)
                .filter(requiredClass::isInstance).map(requiredClass::cast);

    }

    /**
     * Gets the contextualized error message based on a base message. This will add
     * the context label-value pairs to the message.
     *
     * information appended
     *
     * @return the exception message <b>with</b> context information appended, not
     *         {@code null}
     */

    default String getFormattedExceptionMessage() {
        List<Entry> contextEntries = getContextEntries();
        if (contextEntries.isEmpty()) {
            return "";
        }
        final StringBuilder buffer = new StringBuilder(256);

        buffer.append("Exception Context:\n");
        int i = 0;
        for (final Entry pair : contextEntries) {
            buffer.append("\t[");
            i++;
            buffer.append(i);
            buffer.append(':');
            buffer.append(pair.getLabel());
            buffer.append("=");
            try {
                buffer.append(pair.getValue());
            } catch (final Exception e) {
                buffer.append("Exception thrown on toString(): ");
                final StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                buffer.append(sw.getBuffer());
            }
            buffer.append("]\n");
        }
        buffer.append("---------------------------------");

        return buffer.toString();
    }

    default String getFormattedExceptionMessage(final String baseMessage) {
        final StringBuilder buffer = new StringBuilder(256);
        if (baseMessage != null) {
            buffer.append(baseMessage);
        }
        buffer.append('\n');
        buffer.append(getFormattedExceptionMessage());
        return buffer.toString();
    }

}
