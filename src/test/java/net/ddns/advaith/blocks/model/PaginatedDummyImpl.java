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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * A dummy, simple implementation of Paginated that should work. It is just for
 * unit testing purposes.
 */
class PaginatedDummyImpl<T> implements Paginated<T> {
    /**
     * The entries of this dummy. This is a nested list of the elements of the
     * pages.
     */
    private List<List<T>> things;

    /**
     * Keeps track of the current page.
     */
    private int curPage = 0;

    PaginatedDummyImpl(T[]... stuff) {
        things = new ArrayList<List<T>>(stuff.length);
        for (T[] pasty: stuff) {
            things.add(Arrays.<T>asList(pasty));
        }
    }

    @Override
    public boolean hasNextPage() {
        return curPage < things.size();
    }

    @Override
    public List<T> nextPage() {
        if (!hasNextPage()) {
            throw new NoSuchElementException("better quit your job now");
        }
        return things.get(curPage++);
    }

    @Override
    public void setEntriesPerPage(int x) {
        throw new UnsupportedOperationException("do not use :(");
    }
}

