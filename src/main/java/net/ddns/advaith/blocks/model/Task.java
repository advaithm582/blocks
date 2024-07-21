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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Class to represent a single task. The class is method compatible with
 * {@link net.ddns.advaith.blocks.datamodel.TodoData}.
 *
 * @since 0.1.0
 * @author Advaith Menon
 */
public class Task {
    // Constants

    /**
     * Column identifier for ID.
     */
    public static final int ID = 0;

    /**
     * Column identifier for Title.
     */
    public static final int TITLE = 1;

    /**
     * Column identifier for details.
     */
    public static final int DETAILS = 2;

    /**
     * Column identifier for the deadline.
     */
    public static final int DEADLINE = 3;

    // Properties

    /**
     * The unique ID of the task. Has to be a long, so if it's a UUID, please
     * convert it to a number.
     */
    private long id;

    /**
     * The title of the task.
     */
    private String title;

    /**
     * The details of the task.
     */
    private String details;

    /**
     * The deadline of the task.
     */
    private ZonedDateTime deadline;

    /**
     * A zero-argument constructor, only to be used when inheriting. This can
     * make creating constructors with fewer arguments possible in an inherited
     * class. It is also important to know that the behavior of this class is
     * undocumented when using this alone without instantiating required
     * parameters.
     */
    protected Task() {}

    /**
     * A simple 4 argument constructor.
     *
     * @param id The unique ID of the task. Use -1 to indicate absence of a
     * value. Anything lower is invalid.
     * @param title The title of the task.
     * @param details The details of the task.
     * @param deadline The deadline of the task.
     */
    public Task(long id, String title, String details, ZonedDateTime deadline) {
        setID(id);
        setTitle(title);
        setDetails(details);
        setDeadline(deadline);
    }

    /**
     * @return the ID of this task, -1 denotes it's yet to be assigned i.e not
     * stored yet or deleted.
     */
    public long getID() {
        return id;
    }

    /**
     * Set the ID of this task. A valid ID is in the range [0, Long.MAX_VALUE].
     * An ID with a value of -1 denotes the absence of a value. This typically
     * means it's yet to be stored.
     *
     * @param id The ID of this task, in the range [-1, Long.MAX_VALUE].
     * @throws IllegalArgumentException if the ID is less than -1.
     */
    public void setID(long id) {
        if (id < -1) {
            throw new IllegalArgumentException("ID cannot be less than -1.");
        }
        this.id = id;
    }

    /**
     * @return the title of this task.
     * @throws NoSuchElementException if the title is null. This only happens if
     * this method was inherited by a class that didn't use the protected
     * zero-argument constructor correctly.
     */
    public String getTitle() {
        if (title == null) {
            throw new NoSuchElementException();
        }
        return title;
    }

    /**
     * Set the title of this task. The title of this task cannot be an empty
     * string, where an empty string is defined as a string with a null value,
     * or a string with only whitespace characters.
     *
     * @param title The title of this task.
     * @throws IllegalArgumentException if the title is an empty string.
     */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be an empty "
                    + "string.");
        }
        this.title = title;
    }

    /**
     * This is an alias of {@link #getTitle()}. Kept in place for backwards
     * compatibility until version 1.0.0.
     *
     * @return the title of this task.
     * @deprecated since 0.1.0, for removal in 1.0.0. Use {@link #getTitle()} as
     * a replacement.
     */
    @Deprecated(since = "0.1.0", forRemoval = true)
    public String getShortDescription() {
        return getTitle();
    }

    /**
     * This is an alias of {@link #setTitle(java.lang.String)}. Kept in place
     * for backwards compatibility until version 1.0.0.
     *
     * @param shortDescription The title of this task.
     * @deprecated since 0.1.0, for removal in 1.0.0. Use
     * {@link #setTitle(java.lang.String)} as a replacement.
     */
    @Deprecated(since = "0.1.0", forRemoval = true)
    public void setShortDescription(String shortDescription) {
        setTitle(shortDescription);
    }

    /**
     * @return the details of this task.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Set the details of this task. The details of this task cannot be an empty
     * string, where an empty string is defined as a string with only whitespace
     * characters. Note that a null value is OK, it is used to denote the
     * absence of a description.
     *
     * @param details The details of this task.
     * @throws IllegalArgumentException if the details is an empty string.
     */
    public void setDetails(String details) {
        if (details != null && details.isBlank()) {
            throw new IllegalArgumentException("Details cannot be an empty "
                    + "string.");
        }
        this.details = details;
    }

    /**
     * @return the deadline of this task.
     * @throws NoSuchElementException if this null. This only happens if
     * this method was inherited by a class that didn't use the protected
     * zero-argument constructor correctly.
     */
    public ZonedDateTime getDeadline() {
        if (deadline == null) {
            throw new NoSuchElementException();
        }
        return deadline;
    }

    /**
     * Set the deadline of this task. The deadline cannot be null or set to a
     * date in the past.
     *
     * @param deadline The deadline of this task.
     * @throws IllegalArgumentException if the deadline is null or in the past.
     */
    public void setDeadline(ZonedDateTime deadline) {
        if (deadline == null || deadline.compareTo(ZonedDateTime.now()) < 0) {
            throw new IllegalArgumentException("Deadline cannot be null or in "
                    + "the past.");
        }
        this.deadline = deadline;
    }

    /**
     * Get the deadline as a LocalDate. This allows for semi-method
     * compatibility with the old {@link
     * net.ddns.advaith.blocks.datamodel.TodoItem#getDeadline()} method, though
     * the name is different.
     *
     * @return The deadline, as a LocalDate.
     */
    @Deprecated(since = "0.1.0", forRemoval = true)
    public LocalDate getDeadlineDate() {
        return getDeadline().toLocalDate();
    }

    /**
     * An alias for {@link #setDeadline(java.time.LocalDate)}. Kept for
     * backwards-compatibility until version 1.0.0.
     *
     * @param deadline The deadline of this task. It will be internally
     * converted to a ZonedDateTime.
     * @deprecated since 0.1.0, for removal in 1.0.0. Use {@link
     * #setDeadline(java.time.ZonedDateTime)}
     */
    @Deprecated(since = "0.1.0", forRemoval = true)
    public void setDeadline(LocalDate deadline) {
        setDeadline(ZonedDateTime.of(deadline, LocalTime.now(),
                    ZoneId.systemDefault()));
    }

    /**
     * Return a string representation of this task, in the form:
     * <pre>
     * Task[id=1 &lt;or&gt; unset, title="Title" &lt;or&gt; unset, details=first
     * 20 characters of details &lt;or&gt; unset, deadline=2021-01-01T00:00:00Z]
     * </pre>
     *
     * @return a string representation of this task.
     */
    @Override
    public String toString() {
        return "Task[id=" + (id == -1 ? "unset" : id) + ", title="
            + (title == null || title.isBlank() ? "unset" : title) + ", details"
            + "=" + (details == null || details.isBlank() ? "unset" : details)
            + ", deadline=" + (deadline == null ? "unset" 
                    : deadline.toLocalTime().toString()) + "]";
    }
}

