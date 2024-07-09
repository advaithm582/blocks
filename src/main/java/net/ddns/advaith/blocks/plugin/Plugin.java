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

import java.util.UUID;

/**
 * Represents a plugin's entrypoint. All plugins must have a name, version and a
 * UUID. While most other event handlers can be registered using the API, there
 * are three simple event handlers - onInitialize (when the plugin is
 * initialized), onLoad (when the application is loaded) and onClose (when the
 * application is exiting).
 *
 * A plugin can optionally implement a single implementation for any "pluggable"
 * interfaces, like DAO implementations. This is done by implementing one of the
 * get*Factory() methods. Keep in mind that returning a null indicates that your
 * plugin does nothing.
 *
 * Currently, plugins are loaded using a ServiceLoader. However, in the near
 * future, we will use a custom implementation which will also give each plugin
 * its own thread.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public interface Plugin {
    /**
     * @return The name of the plugin. Cannot be null.
     */
    String getName();

    /**
     * @return the UUID of the plugin. Must be unique. It is highly recommended
     * to keep this constant i.e. not generate a random UUID everytime this
     * function is called.
     */
    UUID getUUID();

    /**
     * @return a String containing the version number.
     */
    String getVersion();

    /**
     * Event hook executed when the plugin is loaded.
     */
    void onInitialize();

    /**
     * Event hook executed when the app is loaded. Add GUI elements here.
     */
    void onLoad();

    /**
     * Event hook executed when the application is closed. Use it to close open
     * file/database handles.
     */
    void onClose();

    /**
     * Get the Unified DAO factory. All DAOs are initialized at the same time
     * with use of this factory, and via a proxy object, each individual DAO can
     * be accessed. This has been done because separating the DAOs individually
     * makes no sense.
     */
    DAOFactory getDAOFactory();
}
