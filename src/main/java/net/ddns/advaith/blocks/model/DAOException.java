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
 * Represens an exception raised by a DAO when some sort of database error
 * occurs. For example, if the connection to the database timed out or if the
 * object is too big to store, this is the exception to throw.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public class DAOException extends RuntimeException {
    /**
     * Create a new DAOException.
     *
     * @param message The message to show to the user.
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Create a new DAOException.
     *
     * @param message The message to show to the user.
     * @param cause The cause of the exception.
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
