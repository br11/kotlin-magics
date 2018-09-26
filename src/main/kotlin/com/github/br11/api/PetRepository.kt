package com.github.br11.api

import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

/**
 * Database access for pets.
 */
interface PetRepository {
    /**
     * List pets using start/max limits.
     *
     * @param start Start offset.
     * @param max Max number of rows.
     * @return Available pets.
     */
    @SqlQuery("select * from pets limit :max offset :start")
    fun list(start: Int, max: Int): List<Pet>

    /**
     * Find a pet by ID.
     *
     * @param id Pet ID.
     * @return Pet or null.
     */
    @SqlQuery("select * from pets where id=:id")
    fun findById(id: Long): Pet?

    /**
     * Insert a pet and returns the generated PK.
     *
     * @param pet Pet to insert.
     * @return Primary key.
     */
    @SqlUpdate("insert into pets(name) values(:name)")
    @GetGeneratedKeys
    fun insert(@BindBean pet: Pet): Long

    /**
     * Update a pet and returns true if the pets was updated.
     *
     * @param pet Pet to update.
     * @return True if the pet was updated.
     */
    @SqlUpdate("update pets set name=:name where id=:id")
    fun update(@BindBean pet: Pet): Boolean

    /**
     * Delete a pet by ID.
     *
     * @param id Pet ID.
     * @return True if the pet was deleted.
     */
    @SqlUpdate("delete pets where id=:id")
    fun delete(id: Long): Boolean
}