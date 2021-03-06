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
package org.neo4j.graphalgo;

import org.immutables.value.Value;
import org.jetbrains.annotations.Nullable;
import org.neo4j.graphalgo.annotation.DataClass;
import org.neo4j.graphalgo.core.Aggregation;

import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.emptyMap;
import static org.neo4j.graphalgo.AbstractProjections.PROJECT_ALL;

@DataClass
public abstract class AbstractRelationshipProjection extends ElementProjection {

    private static final RelationshipProjection ALL = of(PROJECT_ALL.name, Orientation.NATURAL);

    public abstract String type();

    @Value.Default
    public Orientation orientation() {
        return Orientation.NATURAL;
    }

    @Value.Default
    public Aggregation aggregation() {
        return Aggregation.DEFAULT;
    }

    @Value.Default
    @Value.Parameter(false)
    @Override
    public PropertyMappings properties() {
        return super.properties();
    }

    @Override
    public boolean projectAll() {
        return type().equals(PROJECT_ALL.name);
    }

    public static final String TYPE_KEY = "type";
    public static final String ORIENTATION_KEY = "orientation";
    public static final String AGGREGATION_KEY = "aggregation";

    public static RelationshipProjection fromMap(Map<String, Object> map, ElementIdentifier identifier) {
        RelationshipProjection.Builder builder = RelationshipProjection.builder();
        String type = String.valueOf(map.getOrDefault(TYPE_KEY, identifier.name));
        builder.type(type);
        if (map.containsKey(ORIENTATION_KEY)) {
            builder.orientation(Orientation.of(nonEmptyString(map, ORIENTATION_KEY)));
        }
        if (map.containsKey(AGGREGATION_KEY)) {
            Aggregation aggregation = Aggregation.lookup(nonEmptyString(map, AGGREGATION_KEY));
            builder.aggregation(aggregation);
            return create(map, aggregation, properties -> builder.properties(properties).build());
        }
        return create(map, properties -> builder.properties(properties).build());
    }

    private static RelationshipProjection create(
        Map<String, Object> config,
        Aggregation defaultAggregation,
        Function<PropertyMappings, RelationshipProjection> constructor
    ) {
        Object properties = config.getOrDefault(PROPERTIES_KEY, emptyMap());
        PropertyMappings propertyMappings = PropertyMappings.fromObject(properties, defaultAggregation);
        return constructor.apply(propertyMappings);
    }


    public static RelationshipProjection fromString(@Nullable String type) {
        return RelationshipProjection.builder().type(type).build();
    }

    public static RelationshipProjection fromObject(Object object, ElementIdentifier identifier) {
        if (object == null) {
            return all();
        }
        if (object instanceof String) {
            return fromString((String) object);
        }
        if (object instanceof Map) {
            @SuppressWarnings("unchecked") Map<String, Object> map = (Map) object;
            return fromMap(map, identifier);
        }
        throw new IllegalArgumentException(String.format(
            "Cannot construct a relationship filter out of a %s",
            object.getClass().getName()
        ));
    }

    public static RelationshipProjection of(String type, Orientation orientation) {
        return RelationshipProjection.builder().type(type).orientation(orientation).build();
    }

    public boolean hasMappings() {
        return properties().hasMappings();
    }

    @Override
    boolean includeAggregation() {
        return true;
    }

    @Override
    void writeToObject(Map<String, Object> value) {
        value.put(TYPE_KEY, type());
        value.put(ORIENTATION_KEY, orientation().name());
        value.put(AGGREGATION_KEY, aggregation().name());
    }

    @Override
    public RelationshipProjection withAdditionalPropertyMappings(PropertyMappings mappings) {
        PropertyMappings withSameAggregation = PropertyMappings
            .builder()
            .from(mappings)
            .withDefaultAggregation(aggregation())
            .build();

        PropertyMappings newMappings = properties().mergeWith(withSameAggregation);

        return newMappings.equals(properties())
            ? RelationshipProjection.copyOf(this)
            : RelationshipProjection.copyOf(this).withProperties(newMappings);
    }

    public static RelationshipProjection all() {
        return ALL;
    }

    public static Builder builder() {
        return new Builder();
    }

    @org.immutables.builder.Builder.AccessibleFields
    public static final class Builder extends RelationshipProjection.Builder implements InlineProperties<Builder> {

        private InlinePropertiesBuilder propertiesBuilder;

        Builder() {
        }

        @Override
        public RelationshipProjection build() {
            buildProperties();
            return super.build();
        }

        @Override
        public InlinePropertiesBuilder inlineBuilder() {
            if (propertiesBuilder == null) {
                propertiesBuilder = new InlinePropertiesBuilder(
                    () -> this.properties,
                    newProperties -> this.properties = newProperties
                );
            }
            return propertiesBuilder;
        }
    }
}
