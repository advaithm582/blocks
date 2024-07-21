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
 * Interface to represent a generic DAO.
 *
 * @since 0.1.0
 * @author Advaith Menon
 * @param <T> The object this DAO deals with
 */
public interface GenericDAO<T> {
    /**
     * Adds a new T to the storage backend.
     *
     * @param item The item to add
     * @throws net.ddns.advaith.blocks.model.DAOException if the operation could
     * not be completed or failed for any reason.
     */
    void add(T item);

    /**
     * Updates all fields of T in the storage backend, based on the ID.
     *
     * @param item The item to update
     * @throws IllegalArgumentException if the ID is -1
     * @throws net.ddns.advaith.blocks.model.DAOException if the operation could
     * not be completed or failed for any reason.
     */
    void update(T item);

    /**
     * Deletes the item T from the storage backend.
     *
     * @param item The item to delete
     * @throws IllegalArgumentException if the ID is -1
     * @throws net.ddns.advaith.blocks.model.DAOException if the operation could
     * not be completed or failed for any reason.
     */
    void delete(T item);

    /**
     * Deletes the item T from the storage backend.
     *
     * @param id The ID of the item to delete
     * @throws IllegalArgumentException if the ID is less than 0
     * @throws net.ddns.advaith.blocks.model.DAOException if the operation could
     * not be completed or failed for any reason.
     */
    void delete(long id);

    /**
     * Get all the items in the storage backend, but in a {@link
     * net.ddns.advaith.blocks.model.Paginated} object. This is definitely more
     * efficient and to be preferred over {@link #fetchAll()}.
     * <b>NOTE:</b> When implementing this, make the Paginated object lazy load
     *
     * @return A paginated version of all the objects.
     * @throws net.ddns.advaith.blocks.model.DAOException if the operation could
     * not be completed or failed for any reason.
     */
    Paginated<T> fetchAll();

    /**
     * Query for a specific object based on a single equals query.
     *
     * @param column The column ID, set in the class. Read the JavaDoc of the
     * respective models.
     * @param target What the column should equal.
     * @return A Paginated of the tasks.
     * @throws net.ddns.advaith.blocks.model.DAOException if the operation could
     * not be completed or failed for any reason.
     */
    Paginated<T> fetchEquals(int column, Object target);

    /**
     * Advanced Select Query. This method will always be optional to implement.
     * However, it hasn't been implemented as of 0.1.0. In the future, we will
     * make use of an inefficient implementation using Paginated objects from
     * the fetchAll method. Currently, it just throws
     * UnsupportedOperationEception.
     *
     * @param query The Select Query to perform.
     * @return a Paginated result set
     * @throws net.ddns.advaith.blocks.model.DAOException if the operation could
     * not be completed or failed for any reason.
     */
    Paginated<T> query(Query query);
}
