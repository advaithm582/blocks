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
import java.util.NoSuchElementException;

/**
 * This class is a default implementation of the paginated iterator. It works by
 * iterating over each of the pages until it is out of pages.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
final class PageIterator<T> implements Iterator<T> {
    // the private Paginated object.
    private final Paginated<T> paginated;

    // current list
    private List<T> current;
    
    // pointer to current element
    private int ptr;

    PageIterator(Paginated<T> paginated) {
        this.paginated = paginated;

        if (paginated.hasNextPage()) {
            current = paginated.nextPage();
        }
    }

    @Override
    public boolean hasNext() {
        if (current != null && ptr >= current.size()) {
            current = paginated.hasNextPage() ? paginated.nextPage() : null;
            ptr = 0;
        }

        return current != null && ptr < current.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return current.get(ptr++);
    }
}
