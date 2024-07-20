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

/**
 * Class to represent a Plugin.
 *
 * This class takes a PluginIdentifier and a Plugin instance and provides a sort
 * of 'flat' view of these objects' data.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public final class PluginWrapper {
    private final PluginIdentifier info;

    private final Plugin plugin;

    /**
     * Create a new PluginWrapper.
     *
     * @param info The PluginIdentifier.
     * @param plugin The Plugin.
     * @throws IllegalArgumentException If info or plugin is null.
     */
    public PluginWrapper(PluginIdentifier info, Plugin plugin) {
        if (info == null) {
            throw new IllegalArgumentException("info cannot be null");
        }
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        this.info = info;
        this.plugin = plugin;
    }

    /**
     * Get info on the plugin.
     *
     * @return The PluginIdentifier.
     */
    public PluginIdentifier getInfo() {
        return info;
    }

    /**
     * Get the name of this plugin.
     *
     * @return The name of this plugin. This cannot be null.
     */
    public String getName() {
        return info.getName();
    }

    /**
     * Get the version of this plugin.
     *
     * @return The version of this plugin. This cannot be null.
     */
    public String getVersion() {
        return info.getVersion();
    }

    /**
     * Get the UUID of this plugin.
     *
     * @return The UUID of this plugin. This cannot be null.
     */
    public java.util.UUID getUUID() {
        return info.getUUID();
    }

    /**
     * Get the plugin.
     *
     * @return The Plugin. This cannot be null.
     */
    public Plugin getPlugin() {
        return plugin;
    }
}
