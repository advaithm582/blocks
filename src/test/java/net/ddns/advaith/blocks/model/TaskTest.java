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

import java.util.NoSuchElementException;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class TaskTest {
    /**
     * Test if the ID setter works as expected. This also makes use of the zero
     * argument constructor for easier testing.
     */
    @Test
    void testSetID() {
        Task t = new Task();
        t.setID(314);
        Assertions.assertEquals(314, t.getID());
        t.setID(-1);
        Assertions.assertEquals(-1, t.getID());
        t.setID(0);
        Assertions.assertEquals(0, t.getID());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {t.setID(-2);});
        Assertions.assertEquals(0, t.getID());
    }

    /**
     * Test the title getters and setters. Even test the so-called
     * "undocumented" behavior of throwing {@link
     * java.util.NoSuchElementException} in case of goof-ups.
     */
    @Test
    void testSetTitle() {
        Task t = new Task();
        Assertions.assertThrows(NoSuchElementException.class,
                () -> {t.getTitle();});
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {t.setTitle(null);});
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {t.setTitle("    ");});
        t.setTitle("Meet with Theodore Roosevelt");
        Assertions.assertEquals("Meet with Theodore Roosevelt",
                t.getTitle());
    }

    /**
     * Test the getting and setting of the description.
     */
    @Test
    void testSetDetails() {
        Task t = new Task();
        t.setDetails("Tell President Truman that Gen. MacArthur was right about"
                + " the Korean war, and if he does not let him do his thing, "
                + "the containment doctrine will not only fail, but also future"
                + " presidents will have to deal with nuclear weapons in the "
                + "hands of a lunatic."); // probably not too good of an idea
                                           // to voice opinions in code, but
                                           // hey! Unit tests are boring, and
                                           // it's not political!
        Assertions.assertEquals("Tell President Truman that Gen. MacArthur was"
                + " right about the Korean war, and if he does not let him do "
                + "his thing, the containment doctrine will not only fail, but "
                + "also future presidents will have to deal with nuclear "
                + "weapons in the hands of a lunatic.", t.getDetails());


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> t.setDetails("    ")); // oops, did s/he see it yet??

        Assertions.assertEquals("Tell President Truman that Gen. MacArthur was"
                + " right about the Korean war, and if he does not let him do "
                + "his thing, the containment doctrine will not only fail, but "
                + "also future presidents will have to deal with nuclear "
                + "weapons in the hands of a lunatic.", t.getDetails());

        t.setDetails(null); // please save us...
        Assertions.assertNull(t.getDetails()); // ah, we are safe!
    }

    /**
     * Test the Deadline's getters and setters.
     */
    @Test
    void testSetDeadline() {
        Task t = new Task();
        Assertions.assertThrows(NoSuchElementException.class,
                () -> t.getDeadline());
        ZonedDateTime unix = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0,
                ZoneId.systemDefault());
        ZonedDateTime tenDays = ZonedDateTime.now().plusDays(10);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> t.setDeadline((ZonedDateTime) null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> t.setDeadline(unix));
        t.setDeadline(tenDays);
        Assertions.assertEquals(tenDays, t.getDeadline());
    }
}
