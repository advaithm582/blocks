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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that parses Plugin Manifests.
 *
 * Plugin Manifests are similar to Windows INI files, except (currently) they
 * lack comments. The main reason I didn't want to use Properties is because of
 * it's inability to handle lists.
 *
 * In Properties files, lists would be handled as follows:
 * <pre>
 * list=1,2,3
 * &lt;OR&gt;
 * list.1=1
 * list.2=2
 * list.3=3
 * </pre>
 * which, to me looks aesthetically DISGUSTING. Consider the INI syntax:
 * <pre>
 * list=1
 * list=2
 * list=3
 * </pre>
 * Much neater!
 *
 * Some caveats that will be fixed in future versions:
 * <ul>
 * <li>Currently, the parser does not support comments.</li>
 * <li>Currently, the parser does not support escaping the equals sign.</li>
 * <li>It does not support string quoting</li>
 * <li>If a list value is "broken" in between, i.e. list=1,jest=2,list=2,list=3,
 * it will still be interpreted as a list. While this might not sound right,
 * trying to fix it might make the code inefficient.<li>
 * </ul>
 *
 * I want to keep the parser simple and fast and free of bloat.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public class PluginManifestParser {
    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(
            PluginManifestParser.class);

    // Pattern to match a section
    private static final Pattern PATTERN_SECTION = Pattern.compile(
            "^\\[([a-zA-Z][a-zA-Z0-9-.]+)\\]$");
    // Pattern to match a key-value pair
    private static final Pattern PATTERN_KV = Pattern.compile(
            "^([a-zA-Z][a-zA-Z0-9-.]+) *= *(.*)$");

    // Map of key-values
    private Map<String, Object> kv;

    // 

    /**
     * Create a new PluginManifestParser.
     */
    PluginManifestParser() {
        kv = new HashMap<>();
    }

    /**
     * Create a new parser from a Reader object.
     *
     * @param reader The reader to create a parser from.
     */
    public PluginManifestParser(Reader reader) throws IOException {
        this();
        parse(reader);
    }

    /**
     * Parse a Plugin Manifest from a Reader.
     *
     * @param reader The reader to read from.
     */
    private void parse(Reader reader) throws IOException {
        try (BufferedReader b = new BufferedReader(reader)) {
            String curSection = "$_DEFAULT";
            int lineN = 1;
            String line;

            while ((line = b.readLine()) != null) {
                Matcher m = PATTERN_SECTION.matcher(line);
                if (m.matches()) {
                    curSection = m.group(1);
                    // create section if not exists
                    if (!kv.containsKey(curSection)) {
                        kv.put(curSection, new ArrayList<>());
                    } else {
                        LOGGER.debug("Plugin Manifest (#{}): Switch section to "
                                + "{}", lineN, curSection);
                        LOGGER.debug("It is not recommended to switch sections "
                                + "in the manifest file. Instead, simply "
                                + "declare those values in the place where the "
                                + "section was first refered. This behavior is "
                                + "subject to change in future versions.");
                    }
                                
                    continue;
                } else if ((m = PATTERN_KV.matcher(line)).matches()) {
                    String key = m.group(1);
                    String value = m.group(2);


                    // check if the current variable already exists as a
                    // singleton, and if so, convert it to a list
                    Object okey = kv.get(key);
                    if (okey != null) {
                        if (okey instanceof List) {
                            ((List) okey).add(value);
                        } else {
                            LOGGER.debug("Plugin Manifest (#{}): Converting {} "
                                    + "to an array. If this was not intended, "
                                    + "don't repeat the key.", lineN, key);
                            List<String> l = new ArrayList<>();
                            l.add((String) okey);
                            l.add(value);
                            kv.put(key, l);
                        }
                    } else {
                        kv.put(key, value);
                    }
                } else {
                    LOGGER.debug("Plugin Manifest: Malformed line #{}"
                            + ", currently considered a \"comment\", behavior "
                            + "subject to change in future versions.", lineN);
                }
                ++lineN;
            }
        }
    }

    /**
     * Get the value given a key and a section. The value will be got as a
     * object (String or List of Strings).
     *
     * @param section The section to look in.
     * @param key The key to look for.
     * @param fallback The value to return if the key is not found.
     * @return The value of the key, or the fallback if the key is not found.
     */
    public Object get(String section, String key, Object fallback) {
        Object o = kv.get(key);
        return o == null ? fallback : o;
    }

    /**
     * Get the value given a key and a section as a string. If the key is not
     * found, or the key is that of a List, then the fallback will be returned.
     *
     * @param section The section to look in.
     * @param key The key to look for.
     * @param fallback The value to return if the key is not found.
     */
    public String getString(String section, String key, String fallback) {
        Object o = get(section, key, null);
        if (o == null || o instanceof List) {
            return fallback;
        } 
        return (String) o;
    }

    /**
     * Get a string from the default section.
     *
     * @param key The key to look for.
     * @param fallback The value to return if the key is not found.
     */
    public String getString(String key, String fallback) {
        return getString("$_DEFAULT", key, fallback);
    }

    /**
     * Get a string from the default section.
     *
     * @param key The key to look for.
     * @return null if the key is not found.
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * Get the value given a key and a section as a list of strings. If the key
     * is not found, the fallback will be returned. If the key is a single
     * string, it will be converted into a list of a single string and returned.
     *
     * @param section The section to look in.
     * @param key The key to look for.
     * @param fallback The value to return if the key is not found.
     */
    public List<String> getList(String section, String key,
            List<String> fallback) {
        Object o = get(section, key, null);
        if (o == null) {
            return fallback;
        } else if (o instanceof List) {
            return (List<String>) o;
        } else {
            List<String> l = new ArrayList<>();
            l.add((String) o);
            return l;
        }
    }

    /**
     * Get a list of strings from the default section.
     *
     * @param key The key to look for.
     * @param fallback The value to return if the key is not found.
     */
    public List<String> getList(String key, List<String> fallback) {
        return getList("$_DEFAULT", key, fallback);
    }

    /**
     * Get a list of strings from any section, with a default fallback of an
     * empty list.
     *
     * @param section The section to look in.
     * @param key The key to look for.
     * @return An empty list if the key is not found.
     */
    public List<String> getList(String section, String key) {
        return getList(section, key, new ArrayList<>());
    }

    /**
     * Get a list of strings from the default section, with a default fallback of
     * an empty list.
     *
     * @param key The key to look for.
     * @return An empty list if the key is not found.
     */
    public List<String> getList(String key) {
        return getList("$_DEFAULT", key);
    }
}
