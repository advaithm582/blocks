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
 * This class provides an immutable view of a plugin's uniquely identifying
 * information and other metadata.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public final class PluginIdentifier {
    private final String name;

    private final String version;

    private final UUID uuid;

    /**
     * Create a new PluginIdentifier.
     *
     * @param name The name of the plugin.
     * @param version The version of the plugin.
     * @param uuid The UUID of the plugin.
     * @throws IllegalArgumentException If name, version, or uuid is null.
     */
    public PluginIdentifier(String name, String version, UUID uuid) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (version == null) {
            throw new IllegalArgumentException("version cannot be null");
        }
        if (uuid == null) {
            throw new IllegalArgumentException("uuid cannot be null");
        }
        this.name = name;
        this.version = version;
        this.uuid = uuid;
    }

    /**
     * @return The name of the plugin. This is never null.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The version of the plugin. This is never null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return The UUID of the plugin. This is never null.
     */
    public UUID getUUID() {
        return uuid;
    }
}
