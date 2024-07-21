/*
 * This file is part of Blocks.
 * 
 * Blocks is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Blocks is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with Blocks. If not, see <https://www.gnu.org/licenses/>.
 */

package net.ddns.advaith.blocks.model;

import java.util.List;
import java.util.LinkedList;

/**
 * This class is used to build queries. While this need not be a SQL query in
 * particular, it is modeled after SQL queries.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public class Query {
    /**
     * Used to represent the array of operators supported by popular database
     * engines.
     *
     * @since 0.1.0
     */
    public static enum Operator {
        EQUALS("="),
        NOT_EQUALS("!="),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        GREATER_THAN_OR_EQUALS(">="),
        LESS_THAN_OR_EQUALS("<="),
        LIKE("LIKE"),
        IN("IN"),
        BETWEEN("BETWEEN"),
        AND("AND"),
        OR("OR"),
        NOT("NOT");

        private final String operator;

        private Operator(String operator) {
            this.operator = operator;
        }

        @Override
        public String toString() {
            return operator;
        }
    }
    
    /**
     * Used to represent the current query type.
     *
     * @since 0.1.0
     */
    public static enum Type {
        SELECT,
        INSERT,
        UPDATE,
        DELETE
    }

    /**
     * Used to represent a WHERE clause in a friendly manner.
     *
     * @since 0.1.0
     */
    public static class Where {
        private final Field field;
        private final Operator operator;
        private final Object value;
        private Operator nextLogicalOperator;

        private Where(Field field, Operator operator, Object value) {
            this.field = field;
            this.operator = operator;
            this.value = value;
        }

        /**
         * @return The field name.
         */
        public Field getField() {
            return field;
        }

        /**
         * @return The operator.
         */
        public Operator getOperator() {
            return operator;
        }

        /**
         * @return The value.
         */
        public Object getValue() {
            return value;
        }

        /**
         * @return The next logical operator.
         */
        public Operator getNextLogicalOperator() {
            return nextLogicalOperator;
        }

        private void setNextLogicalOperator(Operator nextLogicalOperator) {
            this.nextLogicalOperator = nextLogicalOperator;
        }
    }

    static final class WhereBuilder<T> {
        private Field<T> field;

        // the private QueryBuilder object
        private QueryBuilder queryBuilder;

        // the where
        private Where where;

        WhereBuilder(Field<T> field) {
            this.field = field;
        }

        private void setQueryBuilder(QueryBuilder queryBuilder) {
            this.queryBuilder = queryBuilder;
        }

        /**
         * Get an operator and set its value.
         *
         * @param operator The operator, as an enum value.
         * @param value The value to compare the field to.
         * @return the querybuilder object supplied during construction.
         */
        private WhereBuilder is(Operator operator, T value) {
            if (where != null) {
                throw new IllegalStateException("Already set condition - method"
                        + " can only be called once. Use logical operators to "
                        + "combine conditions.");
            }
            where = new Where(field, operator, value);
            return this;
        }

        /**
         * Represents the equals operator.
         *
         * @param value The value to compare the field to.
         * @return the querybuilder object supplied during construction.
         */
        public WhereBuilder equal(T value) {
            return is(Operator.EQUALS, value);
        }

        /**
         * Represents the not equals operator.
         *
         * @param value The value to compare the field to.
         * @return the querybuilder object supplied during construction.
         */
        public WhereBuilder notEquals(T value) {
            return is(Operator.NOT_EQUALS, value);
        }

        /**
         * Represents the greater than operator.
         *
         * @param value The value to compare the field to.
         * @return the querybuilder object supplied during construction.
         */
        public WhereBuilder greaterThan(T value) {
            return is(Operator.GREATER_THAN, value);
        }

        /**
         * Represents the less than operator.
         *
         * @param value The value to compare the field to.
         * @return the querybuilder object supplied during construction.
         */
        public WhereBuilder lessThan(T value) {
            return is(Operator.LESS_THAN, value);
        }

        /**
         * Represents the greater than or equals operator.
         *
         * @param value The value to compare the field to.
         * @return the querybuilder object supplied during construction.
         */
        public WhereBuilder greaterThanOrEquals(T value) {
            return is(Operator.GREATER_THAN_OR_EQUALS, value);
        }

        /**
         * Represents the less than or equals operator.
         *
         * @param value The value to compare the field to.
         * @return the querybuilder object supplied during construction.
         */
        public WhereBuilder lessThanOrEquals(T value) {
            return is(Operator.LESS_THAN_OR_EQUALS, value);
        }

        /**
         * Represents the LIKE operator.
         *
         * @param value The value to compare the field to.
         * @return the querybuilder object supplied during construction.
         */
        public WhereBuilder like(T value) {
            return is(Operator.LIKE, value);
        }

        /**
         * Chains any logical operator to the previous condition, and returns
         * the query builder.
         *
         * @param operator The logical operator.
         * @return the querybuilder object supplied during construction.
         */
        private QueryBuilder chain(Operator operator) {
            if (where == null) {
                throw new IllegalStateException("No condition set - method can "
                        + "only be called after a condition has been set.");
            }
            where.setNextLogicalOperator(operator);
            queryBuilder.where.add(where);
            return queryBuilder;
        }

        /**
         * Chains the AND operator to the previous condition, and returns the
         * query builder.
         *
         * @return the querybuilder object supplied during construction.
         */
        public QueryBuilder and() {
            return chain(Operator.AND);
        }

        /**
         * Chains the OR operator to the previous condition, and returns the
         * query builder.
         *
         * @return the querybuilder object supplied during construction.
         */
        public QueryBuilder or() {
            return chain(Operator.OR);
        }

        /**
         * Chains without any logical operator.
         *
         * @return the querybuilder object supplied during construction.
         */
        public QueryBuilder finish() {
            return chain(null);
        }
    }

    static class QueryBuilder {
        // The query type
        private Type type;

        // The table to perform the query on
        private String table;

        // The list of where clauses
        private List<Where> where;

        private QueryBuilder(Type type) {
            this.type = type;
            where = new LinkedList<>();
        }

        /**
         * Set the table to perform the query on.
         *
         * @param table The table name.
         * @return The QueryBuilder object.
         * @throws IllegalArgumentException if the table name is null or blank.
         */
        public QueryBuilder from(String table) {
            if (table == null || table.isBlank()) {
                throw new IllegalArgumentException("table cannot be null or "
                        + "blank.");
            }
            this.table = table;
            return this;
        }

        /**
         * Begin adding a WHERE clause to the QueryBuilder.
         *
         * @param field The field object.
         * @return The WhereBuilder object.
         */
        public <E> WhereBuilder<E> where(Field<E> field) {
            WhereBuilder<E> wb = field.where();
            wb.setQueryBuilder(this);
            return wb;
        }
    }

    // Fields

    // The query type
    private Type type;

    // The table to perform the query on
    private String table;

    // The list of where clauses
    private List<Where> where;


    // Create a query
    private Query(Type type) {
        this.type = type;
        where = new LinkedList<>();
    }

    /**
     * Create a SELECT query for a table, without specifying a table.
     *
     * @return a new Query object.
     */
    public static final QueryBuilder select() {
        return new QueryBuilder(Type.SELECT);
    }
}
// TODO: implement it
