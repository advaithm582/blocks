
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

package net.ddns.advaith.blocks.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the ConfigManager class.
 * 
 * @since 0.1.0
 * @author Advaith Menon
 */
class ConfigManagerTest {
    @Test
    void testConstructor() throws IOException {
        // test invalid arguments
        // null for both
        assertThrows(IllegalArgumentException.class, () -> {
            new ConfigManager(null, null);
        });

        // empty array, null file
        assertThrows(IllegalArgumentException.class, () -> {
            new ConfigManager(new InputStream[0], null);
        });

        // non-null array, null file (no exception)
        try (ByteArrayInputStream z = new ByteArrayInputStream(
                    "This = is non-political software".getBytes())) {
            ConfigManager m = new ConfigManager(new InputStream[] {z}, null);
            assertEquals("is non-political software",
                    m.get().getProperty("This"));
        }
    }

    @Test
    void testOfFiles(@TempDir File dir) throws IOException {
        // test 0 files - throws error
        assertThrows(IllegalArgumentException.class, () -> {
            ConfigManager.ofFiles();
        });

        // test 1 file - no error
        File f = new File(dir, "lev1.properties");
        try (PrintWriter pw = new PrintWriter(f)) {
            pw.println("test0=1\ntest1=hello");
        }
        ConfigManager cm = ConfigManager.ofFiles(f);
        assertEquals("1", cm.get().getProperty("test0"));
        assertEquals("hello", cm.get().getProperty("test1"));
    }

    @Test
    void testPropertyInheritance(@TempDir File dir) throws IOException {
        // test 2 files - no error
        File f1 = new File(dir, "lev1.properties");
        File f2 = new File(dir, "lev2.properties");
        try (PrintWriter pw = new PrintWriter(f1)) {
            pw.println("test0=1\ntest1=hello");
        }
        try (PrintWriter pw = new PrintWriter(f2)) {
            pw.println("test1=world\ntest2=2");
        }
        ConfigManager cm = ConfigManager.ofFiles(f1, f2);
        assertEquals("1", cm.get().getProperty("test0"));
        assertEquals("world", cm.get().getProperty("test1"));
        assertEquals("2", cm.get().getProperty("test2"));
    }

    @Test
    void testFileSave(@TempDir File dir) throws IOException {
        // test 2 files - no error
        File f1 = new File(dir, "lev1.properties");
        File f2 = new File(dir, "lev2.properties");
        File f3 = new File(dir, "lev3.properties");
        try (PrintWriter pw = new PrintWriter(f1)) {
            pw.println("test0=1\ntest1=hello");
        }
        try (PrintWriter pw = new PrintWriter(f2)) {
            pw.println("test1=world\ntest2=2");
        }
        ConfigManager cm = ConfigManager.ofFiles(f1, f2);
        cm.get().setProperty("test7", "jellyfish");
        cm.save();


        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream(f2)) {
            p.load(fis);
        }
        assertEquals(cm.get(), p);

        cm.save(1, f3);

        p = new Properties();
        try (FileInputStream fis = new FileInputStream(f3)) {
            p.load(fis);
        }
        assertEquals(cm.get(), p);

        cm.save(0, f3);
        p = new Properties();
        try (FileInputStream fis = new FileInputStream(f1)) {
            p.load(fis);
        }
        assertEquals(cm.get(0), p);

        try (FileInputStream c1 = new FileInputStream(f1);
                FileInputStream c2 = new FileInputStream(f2)) {
            ConfigManager cd = new ConfigManager(new InputStream[] {c1, c2},
                    null);
            cd.save(); // silent failure
        }
    }
}

