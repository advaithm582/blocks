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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ddns.advaith.blocks.config.ConfigManager;

/**
 * Class that loads plugins.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public class PluginLoader {
    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(
            PluginLoader.class);

    // Current Instance
    private static PluginLoader instance;

    // List of all the loaded plugins. This is a HashMap from UUID to Plugin.
    private Map<UUID, PluginWrapper> plugins;

    /**
     * Get an instance of the PluginLoader class. This works by loading all
     * plugins from $INSTALLDIR/plugins, and the configuration property {@code
     * net.ddns.advaith.blocks.pluginsDir}, if it exists.
     *
     * @return A cached version of the PluginLoader, which is instantiated once
     * using the above process. It is not possible to run this process again
     * after the plugins have been loaded. This will never be null.
     * @throws NullPointerException if there was any error in creating the
     * object (thus leaving it internally as null).
     */
    public static PluginLoader getInstance() {
        if (instance == null) {
            instance = new PluginLoader();
            Path primary;
            try {
                primary = new File(PluginLoader.class
                        .getProtectionDomain().getCodeSource().getLocation()
                        .toURI()).getParentFile().toPath();
            } catch (URISyntaxException e) {
                LOGGER.error("Could not get primary directory", e);
                throw new NullPointerException("Could not create instance");
            }
            instance.loadPluginsFromDirectory(primary.resolve("plugins"), true);
            String pluginsDir = ConfigManager.getInstance().getProperty(
                    "net.ddns.advaith.blocks.pluginsDir");
            if (pluginsDir != null) {
                instance.loadPluginsFromDirectory(Paths.get(pluginsDir), true);
            }
        }
        return instance;
    }


    /**
     * Create a new PluginLoader.
     */
    public PluginLoader() {
        plugins = new HashMap<>();
    }

    /**
     * Load a plugin's metadata file.
     *
     * @param jar Path to a JAR file.
     * @return A PluginManifest.
     * @throws PluginLoadException If the metadata file could not be loaded.
     */
    public static PluginManifestParser loadManifest(Path jar)
            throws PluginLoadException {
        try (Reader r = new InputStreamReader(new URL(
                    "jar:" + jar.toUri().toString() 
                    + "!/META-INF/blocks/manifest.ini").openStream())) {
            return new PluginManifestParser(r);
        } catch (IOException e) {
            throw new PluginLoadException("Could not load manifest for " + jar,
                    e);
        }
    }

    /**
     * Load a single plugin from a JAR file. If the plugin lacks a manifest, or
     * if it does not exist, the exception will be thrown.
     *
     * @param parser The parsed manifest of the plugin.
     * @return The PluginWrapper for the plugin.
     */
    public static PluginIdentifier loadIdentifiers(PluginManifestParser parser)
            throws PluginLoadException {
        String name = parser.getString("name");
        String version = parser.getString("version");
        String uuidStr = parser.getString("uuid");

        if (name == null || version == null || uuidStr == null || name.isEmpty()
                || uuidStr.isEmpty()) {
            throw new PluginLoadException("One of name, version, UUID is not "
                    + "defined in the manifest");
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            throw new PluginLoadException("UUID is not a valid UUID", e);
        }

        String entrypoint = parser.getString("classes", "entrypoint");

        if (entrypoint == null || entrypoint.isEmpty()) {
            throw new PluginLoadException("Failed to get entrypoint");
        }

        return new PluginIdentifier(name, version, uuid);
    }

    /**
     * Load a single plugin from a directory. This directory should contain a
     * JAR file which starts with the name of the folder. The name of the folder
     * itself should be the plugin's UUID or its name in lowercase, with spaces
     * substituted with hyphens.
     *
     * @param path The path to the directory.
     * @return The PluginWrapper for the plugin.
     * @throws PluginLoadException If the plugin could not be loaded.
     */
    public static PluginWrapper loadPlugin(Path path)
            throws PluginLoadException {
        if (!Files.isDirectory(path)) {
            throw new PluginLoadException("Path is not a directory");
        }

        String x = path.getFileName().toString().toLowerCase();
        String search_;
        boolean exact_;
        try {
            search_ = UUID.fromString(x).toString();
            exact_ = true;
        } catch (IllegalArgumentException e) {
            search_ = x.replace(' ', '-');
            exact_ = false;
        }

        final String search = search_;
        final boolean exact = exact_;

        Path jar;

        try {
            jar = Paths.get(
                    Files.walk(path, 1).filter(a -> a.getParent().equals(path))
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(a -> exact ? a.equals(search + ".jar")
                        : a.startsWith(search))
                .findFirst().get());
        } catch (IOException e) {
            throw new PluginLoadException("Error while loading JAR file", e);
        } catch (NoSuchElementException e) {
            throw new PluginLoadException("Could not find JAR file");
        }

        PluginManifestParser parser = loadManifest(jar);
        PluginIdentifier info = loadIdentifiers(parser);

        URLClassLoader pLoader;
        try {
            pLoader = new URLClassLoader(
                Files.walk(path, 1)
                    .filter(a -> a.getParent().equals(path))
                    .map(Path::toUri)
                    .map(a -> {
                        try {
                            return a.toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }})
                    .toArray(URL[]::new), null);
        } catch (IOException e) {
            throw new PluginLoadException("Error while loading classloader", e);
        }

        Class plugin;

        try {
            plugin = Class.forName(parser.getString("classes", "entrypoint"),
                    false, pLoader);
        } catch (ClassNotFoundException e) {
            throw new PluginLoadException("Could not find entrypoint class", e);
        }

        if (!Plugin.class.isAssignableFrom(plugin)) {
            throw new PluginLoadException("Entrypoint class is not a plugin");
        }

        Plugin pluginInstance;

        Constructor<Plugin> constructor;
        try {
            constructor = plugin.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new PluginLoadException("Could not find no arg constructor", e);
        }

        try {
            pluginInstance = (Plugin) constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new PluginLoadException("Could not instantiate plugin", e);
        }

        return new PluginWrapper(info, pluginInstance);
    }

    /**
     * Load all plugins from a directory.
     *
     * @param path The path to the directory.
     * @param silentFail Whether to silently fail in case of a loading error.
     */
    public void loadPluginsFromDirectory(Path path, boolean silentFail) {
        try {
            Files.walk(path, 1)
                .filter(Files::isDirectory)
                .forEach(a -> {
                    try {
                        PluginWrapper wrapper = loadPlugin(a);
                        plugins.put(wrapper.getUUID(), wrapper);
                    } catch (PluginLoadException e) {
                        LOGGER.error("Could not load plugin", e);
                        if (!silentFail) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        } catch (IOException e) {
            LOGGER.error("Error while loading plugins", e);
            if (!silentFail) {
                throw new RuntimeException(e);
            }
        }
    }
}
