package com.github.br11.api

import org.jooby.*
import org.jooby.apitool.ApiTool
import org.jooby.json.Jackson

/**
 * Kotlin ApiTool.
 */
class AppApi : Kooby({

    /** JSON: */
    use(Jackson())

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
            listOf(Pet(1, "Rex"))
        }

        /**
         * Find pet by ID
         *
         * @param id Pet ID.
         * @return Returns `200` with a single pet or `404`
         */
        get("/:id") {
            val id = param<Long>("id")
            Pet(id, "Rex")
        }

        /**
         * Add a new pet to the store.
         *
         * @param body Pet object that needs to be added to the store.
         * @return Returns a saved pet.
         */
        post {
            val pet = body<Pet>()

            Pet(1, pet.name)
        }

        /**
         * Update an existing pet.
         *
         * @param body Pet object that needs to be updated.
         * @see Pet
         * @return Returns a saved pet.
         */
        put {
            val pet = body<Pet>()
            if (!pet.id.equals(1)) {
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
            val id = param<Long>("id")

            if (!id.equals(1)) {
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
    run(::AppApi, *args)
}