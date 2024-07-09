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

import net.ddns.advaith.blocks.model.GenericDAO;
import net.ddns.advaith.blocks.model.Task;

/**
 * Allows accessing of multiple DAOs (Task, Category, Block etc.) through a
 * single proxy object.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public interface DAOProxy {
    /**
     * Return a DAO for a Task.
     *
     * @return The Task DAO.
     */
    GenericDAO<Task> getTaskDAO();
}
