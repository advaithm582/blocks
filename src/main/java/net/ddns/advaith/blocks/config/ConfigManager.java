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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Simple class to enable hierarchical loading of properties files from multiple
 * locations. This loader can load configuration files from n locations and
 * merge them together to form a single propeties.
 *
 * Currently, programmatic modification of configuration is not supported.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public class ConfigManager {
    // the list of configuration files to load
    private InputStream[] inputs;

    // the File to save the configuration files to.
    private File saveLoc;

    // the properties instances corresponding to each of these files
    private Properties[] props;

    /**
     * Create a new Configuration Manager.
     *
     * @param inputs The InputStreams to load the configuration from.
     * @param saveLoc The File to save the configuration to.
     * @throws IOException if there was an error encountered by
     * java.util.Properties while reading the file. To avoid this, use the
     * Builder class, which comes with a silent failure mode.
     * @throws IllegalArgumentException if inputs/saveLoc is null or empty.
     */
    public ConfigManager(InputStream[] inputs, File saveLoc) 
            throws IOException {
        if (inputs == null || inputs.length == 0) {
            throw new IllegalArgumentException("inputs cannot be null/empty");
        } else if (saveLoc == null) {
            throw new IllegalArgumentException("saveLoc cannot be null");
        }

        this.inputs = inputs;

        // ref to saveLoc
        this.saveLoc = saveLoc;

        load();
    }

    /**
     * Create a new Configuration Manager.
     *
     * @param files The files to load configuration from. This must be in the
     * order of highest level (system-wide) to lowest level (current-directory).
     * @throws FileNotFoundException if any of the configuration files are not
     * found.
     * @throws IOException if there was an error encountered by
     * java.util.Properties
     * @throws IllegalArgumentException if files is null or empty.
     */
    public ConfigManager(File... files)
            throws FileNotFoundException, IOException {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("files cannot be null/empty");
        }

        // create an array of InputStreams
        InputStream[] inputs = new InputStream[files.length];
        for (File f: files) {
            inputs[i] = new FileInputStream(f);
        }

        this(inputs, files[files.length - 1]);
    }

    /**
     * Load the configuration from the input streams. This might make no
     * difference if the file was modified, since Files are discarded at the
     * constructor stage.
     *
     * @throws IOException if there was an error encountered by java Properties
     */
    private void load() throws IOException {
        // create a new Properties array
        props = new Properties[inputs.length];

        props[0] = new Properties();
        props[0].load(inputs[0]);

        for (int i = 1; i < inputs.length; ++i) {
            props[i] = new Properties(props[i - 1]);
            props[i].load(inputs[i]);
        }
    }

    /**
     * Get the merged configuration.
     *
     * @return The lowermost level of the Properites.
     * @implNote Internally coded using the {@link #get(int)} method. No need to
     * override when inheriting.
     */
    public Properties get() {
        return get(props.length - 1);
    }

    /**
     * Get a given level of the configuration.
     *
     * @param i The level of the configuration to get.
     * @return The merged Properties at that level.
     * @throws IllegalArgumentException if i is out of bounds.
     */
    public Properties get(int i) {
        if (i < 0 || i >= props.length) {
            throw new IllegalArgumentException("i is out of bounds");
        }

        return props[i];
    }

    /**
     * Save any given configuration level to any file.
     *
     * @param i The level of the configuration to save.
     * @param loc The file to save the configuration to.
     * @throws IOException if there was an error encountered by Properties.
     * @throws FileNotFoundException if the file is not found.
     * @throws IllegalArgumentException if i is out of bounds or loc is null.
     */
    public void save(int i, File loc)
            throws IOException, FileNotFoundException {
        if (i < 0 || i >= props.length) {
            throw new IllegalArgumentException("i is out of bounds");
        } else if (loc == null) {
            throw new IllegalArgumentException("loc cannot be null");
        }

        try (FileOutputStream fos = new FileOutputStream(loc)) {
            props[i].store(fos, "Machine generated file. Do not modify.");
        }
    }

    /**
     * Save the lowermost level of the configuration to the file specified while
     * constructing this class. This method fails silently - check log messages
     * for any errors. For a non-silent failure, use the {@link
     * #save(int,java.io.File)} method.
     */
    public void save() {
        try {
            save(props.length - 1, saveLoc);
        } catch (IOException | FileNotFoundException e) {
            // TODO: log using SLF4J
        }
    }

     
}
