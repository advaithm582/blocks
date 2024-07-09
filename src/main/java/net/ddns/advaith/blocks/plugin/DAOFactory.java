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

package net.ddns.advaith.blocks.plugin;

/**
 * Factory for a DAOProxy object. The only useful constructor setting I could
 * think of was setting the number of entities per page, since the settings
 * could be fetched using the API anyway. The software uses .build() to create a
 * new DAOProxy object, which basically loads the necessary DAOs.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public interface DAOFactory {
    /**
     * Set the number of elements per page each Paginated object should have.
     * This method is optional to implement.
     *
     * @param x The number of elements per page. Must be greater than 0.
     * @throws IllegalArgumentException when x is less than or equal to 0
     * @throws UnsupportedOperationException by default
     */
    default void setNumPerPage(int x) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create the new DAOProxy object.
     *
     * @return A fully functional DAOProxyObject.
     */
    DAOProxy build();
}

