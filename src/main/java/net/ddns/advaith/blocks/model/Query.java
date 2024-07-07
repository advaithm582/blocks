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

/**
 * This class is used to build queries. While this need not be a SQL query in
 * particular, it is modeled after SQL queries.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public class Query {
    /**
     * Types of queries.
     */
    public enum Type {
        SELECT,
        UPDATE,
        DELETE;
    }

    /**
     * Represents the possible SQL binary operators, specifically comparison.
     */
    public enum Comparison {
        /**
         * Less than.
         */
        LT,

        /**
         * Less than equal.
         */
        LTE,

        /**
         * Greater than
         */
        GT,

        /**
         * Greater than equal.
         */
        GTE,

        /**
         * Equal.
         */
        EQ;
    }

    /**
     * Represents a Where query condition.
     */
    public record Condition (int field, Comparison operator, Object value) {}

    /**
     * Represents an Assignment.
     */
    public record Assignment (int field, Object value) {}

    /**
     * The query type.
     */
    private Type type;

    /**
     * A list of 'where' condition maps.
     */
    private List<Condition> conditions;

    /**
     * A list of assignments, for UPDATE and DELETE.
     */
    private List<Assignment> assignments;

}
// TODO: implement it
