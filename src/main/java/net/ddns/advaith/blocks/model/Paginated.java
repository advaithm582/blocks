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
import java.util.Iterator;

/**
 * Interface to represent a paginated result. While we could have just gone for
 * a straightforward approach with {@link java.lang.Iterable}, we chose to
 * create some sort of wrapper interface around it. One good reason why this
 * might be a good idea is that if one is displaying a paginated result set in a
 * GUI, this will make it easy rather than having to iterate n times and then
 * retrieve the pages.
 *
 * This class still implements the Iterator, though it is not recommended.
 * @since 0.1.0
 * @author Advaith Menon
 * @param <T> The object this Paginated returns a list of
 */
public interface Paginated<T> extends Iterable {
    /**
     * Set the number of entries returned per page. This can only be changed
     * until the first call of the {@link #nextPage()} method - as soon as that
     * is called, this method will throw an IllegalStateException
     * instead.
     *
     * <b>Note:</b> If you are writing an implementation, be sure to take the
     * defaut value of this class from (TODO: fill the class name here) with the
     * key (TODO:fill the key here). DO NOT HARDCODE A DEFAULT VALUE!
     *
     * @param x The number of entries per page
     */
    void setEntriesPerPage(int x);

    /**
     * @return if there is a next page.
     */
    boolean hasNextPage();

    /**
     * @return the next page, if it exists
     * @throws java.util.NoSuchElementException if all pages are exhausted i.e.
     * there are no more pages to display.
     */
    List<T> nextPage();

    @Override
    default Iterator<T> iterator() {
        return new PageIterator<T>(this);
    }
}

