/*
 * Copyright (c) 2017-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
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
package positive;

import org.jetbrains.annotations.NotNull;
import org.neo4j.graphalgo.core.CypherMapWrapper;

import javax.annotation.processing.Generated;

@Generated("org.neo4j.graphalgo.proc.ConfigurationProcessor")
public final class RangeValidationConfig implements RangeValidation {

    private final int integerWithinRange;
    private final double doubleWithinRange;

    public DefaultValuesConfig(@NotNull CypherMapWrapper config) {
        this.integerWithinRange = CypherMapWrapper.validateIntegerRange("integerWithinRange", config.requireInt("integerWithinRange"), 21, 42, false, true);
        this.doubleWithinRange = CypherMapWrapper.validateDoubleRange("doubleWithinRange", config.requireDouble("doubleWithinRange"), 21.0, 42.0, false, true);
    }

    @Override
    public int integerWithinRange() {
        return this.integerWithinRange;
    }

    @Override
    public double doubleWithinRange() {
        return this.doubleWithinRange;
    }
}
