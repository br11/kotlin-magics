package com.github.br11.kondoocthor.customer.api

import com.github.br11.kondoocthor.customer.domain.CorporateCustomer
import com.github.br11.kondoocthor.customer.domain.CorporateCustomerService
import com.github.br11.kondoocthor.customer.domain.NewCorporateCustomerService
import org.jooby.*
import org.jooby.apitool.ApiTool
import org.jooby.json.Jackson

/**
 * Kotlin ApiTool.
 */
class CustomerApi : Kooby({

    /** JSON: */
    use(Jackson())

    /** Export API to Swagger and RAML: */
    use(ApiTool()
            .filter { r -> r.pattern().startsWith("/api") }
            .swagger()
            .raml())

    // Home page redirect to Swagger:
    get { Results.redirect("/swagger") }

    path("/api/customer") {

        /**
         * List Corporate Customers ordered by ULID.
         *
         * @param start Start offset, useful for paging. Default is `0`.
         * @param max Max page size, useful for paging. Default is `20`.
         * @return Corporate Customers ordered by ULID.
         */
        get {
            listOf(CorporateCustomer("askdjkjasl;dk[u00uweifj09u0"),
                    CorporateCustomer("askdjkjasl;dk[sadfasd"),
                    CorporateCustomer("askdjkjasl;asdf[adsfaasd"),
                    CorporateCustomer("askdjkjasl;dk[ads"),
                    CorporateCustomer("qe34;dk[u01430uweifj09u0"),
                    CorporateCustomer(";234fwer[u0143wer0uweifj09u0"))
        }

        /**
         * Find CorporateCustomer by ULID
         *
         * @param ulid CorporateCustomer ULID.
         * @return Returns `200` with a single CorporateCustomer or `404`
         */
        get("/:ulid") {
            val ulid = param<String>("ulid")

            CorporateCustomer(ulid)
        }

        /**
         * Add a new CorporateCustomer to the database.
         *
         * @param body CorporateCustomer object that needs to be added to the database.
         * @return Returns a saved CorporateCustomer.
         */
        post {
            val request = body<CorporateCustomer>()

            CorporateCustomerService().saveApproved(request)
        }

        /**
         * Update an existing CorporateCustomer.
         *
         * @param body CorporateCustomer object that needs to be updated.
         * @see Pet
         * @return Returns a saved pet.
         */
        put("/:ulid") {
            val id = param<String>("ulid")

            val request = body<CorporateCustomer>()

            NewCorporateCustomerService().execute(request)
        }

        /**
         * Deletes a CorporateCustomer by ULID.
         *
         * @param id CorporateCustomer ULID.
         * @return A `204`
         */
        delete("/:ulid") {
            val id = param<String>("ulid")

            val request = body<CorporateCustomer>()

            NewCorporateCustomerService().execute(request)

            Results.noContent()
        }
    }
})

/**
 * Run application:
 */
fun main(args: Array<String>) {
    run(::CustomerApi, *args)
}