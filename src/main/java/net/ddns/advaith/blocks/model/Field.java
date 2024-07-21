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

/**
 * This class represents a field in a database table.
 *
 * @param <T> Denotes the Java equivalent of the field's datatype.
 * @since 0.1.0
 * @author Advaith Menon
 */
public final class Field<T> {
    // The field name
    private final String name;

    // The table this field belongs to
    private final String table;

    /**
     * Create a new field. This is to be only used by classes implementing a
     * object for a corresponding database table.
     *
     * @param name The name of the field.
     * @param table The table this field belongs to.
     * @throws IllegalArgumentException if either of those are null or blank.
     */
    public Field(String name, String table) {
        if (name == null) {
            throw new IllegalArgumentException("Field name cannot be null.");
        } else if (name.isBlank()) {
            throw new IllegalArgumentException("Field name cannot be blank.");
        } else if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null.");
        } else if (table.isBlank()) {
            throw new IllegalArgumentException("Table name cannot be blank.");
        }

        this.name = name;
        this.table = table;
    }

    /**
     * Create a new WhereBuilder for this field. This is mainly done for the
     * enablement of Java generics.
     *
     * @return A new WhereBuilder for this field.
     */
    Query.WhereBuilder<T> where() {
        return new Query.WhereBuilder<T>(this);
    }
}
