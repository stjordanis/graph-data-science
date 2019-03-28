/*
 * Copyright (c) 2017 "Neo4j, Inc." <http://neo4j.com>
 *
 * This file is part of Neo4j Graph Algorithms <http://github.com/neo4j-contrib/neo4j-graph-algorithms>.
 *
 * Neo4j Graph Algorithms is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.graphalgo.core.utils.paged;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class HugeLongArrayTest extends HugeArrayTestBase<long[], Long, HugeLongArray> {

    @Test
    public void shouldBinaryOrValues() {
        testArray(10, array -> {
            int index = between(2, 8);
            int value = between(42, 1337);
            array.set(index, value);
            int newValue = between(42, 1337);
            array.or(index, newValue);
            assertEquals(value | newValue, array.get(index));
        });
    }

    @Test
    public void shouldBinaryAndValues() {
        testArray(10, array -> {
            int index = between(2, 8);
            int value = between(42, 1337);
            array.set(index, value);
            int newValue = between(42, 1337);
            array.and(index, newValue);
            assertEquals(value & newValue, array.get(index));
        });
    }

    @Test
    public final void shouldAddToValues() {
        testArray(10, array -> {
            int index = between(2, 8);
            int value = between(42, 1337);
            array.set(index, value);
            int newValue = between(42, 1337);
            array.addTo(index, newValue);
            assertEquals(value + newValue, array.get(index));
        });
    }

    @Override
    HugeLongArray singleArray(final int size) {
        return HugeLongArray.newSingleArray(size, AllocationTracker.EMPTY);
    }

    @Override
    HugeLongArray pagedArray(final int size) {
        return HugeLongArray.newPagedArray(size, AllocationTracker.EMPTY);
    }

    @Override
    long bufferSize(final int size) {
        return MemoryUsage.sizeOfLongArray(size);
    }

    @Override
    Long box(final int value) {
        return (long) value;
    }

    @Override
    int unbox(final Long value) {
        return value.intValue();
    }
}