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

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test if the PageIterator works as intended. By 'intended', we mean that all
 * pages have the same number of entries, except the last one, which has the
 * same or lesser number of entries. Why does this mattter? Our
 * PaginatedDummyImpl doesn't paginate automatically for us - we provide the
 * pages to it in the constructor.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
class PageIteratorTest {
    /**
     * Test if the Iterator works with a blank Paginated object.
     * <ol>
     * <li>It should give a hasNext() as false.</li>
     * <li>It should throw NoSuchElementException on next().</li>
     * </ol>
     */
    @Test
    void testBlank() {
        Paginated<Integer> p = new PaginatedDummyImpl<Integer>();

        // note: {} will not be a blank paginated as it has one page.
        // there should be 0 pages.

        Iterator<Integer> i = p.iterator();

        assertFalse(i.hasNext());

        assertThrows(NoSuchElementException.class, () -> i.next());
    }

    /**
     * Test if the Iterator works with pages of size 1. We assume that it will
     * work for pages of size n too, but we still go ahead and test it with one
     * page of size 5 and three pages with the last one of a variable size.
     */
    @Test
    void testFiveOne() {
        Paginated<Integer> p = new PaginatedDummyImpl<Integer>(
                new Integer[] {1}, new Integer[] {2}, new Integer[] {3},
                new Integer[] {4}, new Integer[] {5});

        Iterator<Integer> i = p.iterator();

        assertTrue(i.hasNext(), "at 1");
        assertEquals(1, i.next());

        assertTrue(i.hasNext(), "at 2");
        assertEquals(2, i.next());

        assertTrue(i.hasNext(), "at 3");
        assertEquals(3, i.next());

        assertTrue(i.hasNext(), "at 4");
        assertEquals(4, i.next());

        assertTrue(i.hasNext(), "at 5");
        assertEquals(5, i.next());

        assertFalse(i.hasNext(), "the end");
        assertThrows(NoSuchElementException.class, () -> i.next());
    }

    /**
     * Test a single page of five.
     */
    @Test
    void testOneFive() {
        Paginated<Integer> p = new PaginatedDummyImpl<Integer>(
            new Integer[] {1, 2, 3, 4, 5});

        Iterator<Integer> i = p.iterator();

        assertTrue(i.hasNext(), "at 1");
        assertEquals(1, i.next());

        assertTrue(i.hasNext(), "at 2");
        assertEquals(2, i.next());

        assertTrue(i.hasNext(), "at 3");
        assertEquals(3, i.next());

        assertTrue(i.hasNext(), "at 4");
        assertEquals(4, i.next());

        assertTrue(i.hasNext(), "at 5");
        assertEquals(5, i.next());

        assertFalse(i.hasNext(), "the end");
        assertThrows(NoSuchElementException.class, () -> i.next());
    }

    /**
     * Test two pages of three, then last page of one.
     */
    @Test
    void testTwoThreeOne() {
        Paginated<Integer> p = new PaginatedDummyImpl<Integer>(
            new Integer[] {1, 2, 3}, new Integer[] {4, 5, 6},
            new Integer[] {7});

        Iterator<Integer> i = p.iterator();

        assertTrue(i.hasNext(), "at 1");
        assertEquals(1, i.next());

        assertTrue(i.hasNext(), "at 2");
        assertEquals(2, i.next());

        assertTrue(i.hasNext(), "at 3");
        assertEquals(3, i.next());

        assertTrue(i.hasNext(), "at 4");
        assertEquals(4, i.next());

        assertTrue(i.hasNext(), "at 5");
        assertEquals(5, i.next());

        assertTrue(i.hasNext(), "at 6");
        assertEquals(6, i.next());

        assertTrue(i.hasNext(), "at 7");
        assertEquals(7, i.next());

        assertFalse(i.hasNext(), "the end");
        assertThrows(NoSuchElementException.class, () -> i.next());
    }
}
