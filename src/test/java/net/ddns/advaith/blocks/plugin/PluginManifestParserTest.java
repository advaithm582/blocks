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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for the PluginManifestParser class.
 */
class PluginManifestParserTest {
    private InputStreamReader testR;

    @BeforeEach
    void setUp() throws Exception {
        testR = new InputStreamReader(getClass().getResourceAsStream(
                    "PluginManifestParserTest_manifest.ini"));
    }

    @AfterEach
    void tearDown() throws Exception {
        testR.close();
    }

    @Test
    void testGet() throws Exception {
        PluginManifestParser parser = new PluginManifestParser(testR);
        assertEquals("yes", parser.get("$_DEFAULT", "defaultSectionValidKey",
                    null));
        assertNull(parser.get("$_DEFAULT", "1thisisinvalid", null));
        assertEquals(List.of("1", "2", "3"), parser.getList("$_DEFAULT",
                    "list", null));
    }

    @Test
    void testGetString() throws Exception {
        PluginManifestParser parser = new PluginManifestParser(testR);
        assertEquals("yes . and these spaces count:   ",
                parser.getString("$_DEFAULT", "this.is.also.ok", null));
        assertEquals("fb", parser.getString("$_DEFAULT", "this$is$not$ok",
                    "fb"));
        assertEquals("yal", parser.getString("ValidSection", "ValidKey", 
                    "yal"));
        assertEquals("yes", parser.getString("ValidSection", "StillValid",
                    null));
        assertEquals("", parser.getString("ValidSection", "Still.in.previous",
                    null));
        assertEquals("yes = it works! ; this no comment.", parser.getString(
                    "ValidSection", "TestEqualsInCfg", null));
        assertEquals("y", parser.getString("Valid.section-name", "test", null));
    }

    @Test
    void getList() throws Exception {
        PluginManifestParser parser = new PluginManifestParser(testR);
        assertEquals(List.of("1", "2", "3"), parser.getList("$_DEFAULT",
                    "list", null));
        assertEquals(List.of("yes", "now I am a list"), parser.getList(
                    "ValidSection", "ValidKey", null));
        assertEquals(List.of("1"), parser.getList("$_DEFAULT",
                    "this-pattern-ok", null));
    }
}
