package com.github.br11.api

import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.jooby.*
import org.jooby.apitool.ApiTool
import org.jooby.flyway.Flywaydb
import org.jooby.jdbc.Jdbc
import org.jooby.jdbi.Jdbi3
import org.jooby.jdbi.TransactionalRequest
import org.jooby.json.Jackson

/**
 * Kotlin ApiTool.
 */
class App : Kooby({

    /** JSON: */
    use(Jackson())

    /** Database: */
    use(Jdbc())
    use(Flywaydb())
    use(Jdbi3()
            .doWith { jdbi ->
                jdbi.installPlugin(SqlObjectPlugin())
                jdbi.installPlugin(KotlinPlugin())
                jdbi.installPlugin(KotlinSqlObjectPlugin())
            }
            /** Simple transaction per request and bind the PetRepository to it:  */
            .transactionPerRequest(
                    TransactionalRequest()
                            .attach(PetRepository::class.java)
            ))

    /** Export API to Swagger and RAML: */
    use(ApiTool()
            .filter { r -> r.pattern().startsWith("/api") }
            .swagger()
            .raml())

    // Home page redirect to Swagger:
    get { Results.redirect("/swagger") }

    /**
     * Everything about your Pets.
     */
    path("/api/pet") {
        /**
         * List pets ordered by id.
         *
         * @param start Start offset, useful for paging. Default is `0`.
         * @param max Max page size, useful for paging. Default is `20`.
         * @return Pets ordered by name.
         */
        get {
            val db = require(PetRepository::class)

            val start = param("start").intValue(0)
            val max = param("max").intValue(20)

            db.list(start, max)
        }

        /**
         * Find pet by ID
         *
         * @param id Pet ID.
         * @return Returns `200` with a single pet or `404`
         */
        get("/:id") {
            val db = require(PetRepository::class)

            val id = param<Long>("id")

            val pet = db.findById(id) ?: throw Err(Status.NOT_FOUND)

            pet
        }

        /**
         * Add a new pet to the store.
         *
         * @param body Pet object that needs to be added to the store.
         * @return Returns a saved pet.
         */
        post {
            val db = require(PetRepository::class)

            val pet = body<Pet>()

            val id = db.insert(pet)

            Pet(id, pet.name)
        }

        /**
         * Update an existing pet.
         *
         * @param body Pet object that needs to be updated.
         * @see Pet
         * @return Returns a saved pet.
         */
        put {
            val db = require(PetRepository::class)

            val pet = body<Pet>()
            if (!db.update(pet)) {
                throw Err(Status.NOT_FOUND)
            }
            pet
        }

        /**
         * Deletes a pet by ID.
         *
         * @param id Pet ID.
         * @return A `204`
         */
        delete("/:id") {
            val db = require(PetRepository::class)

            val id = param<Long>("id")

            if (!db.delete(id)) {
                throw Err(Status.NOT_FOUND)
            }
            Results.noContent()
        }
    }
})

/**
 * Run application:
 */
fun main(args: Array<String>) {
    run(::App, *args)
}
