package com.github.br11.api


import io.restassured.RestAssured.given
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jooby.Status
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
object AppTest : Spek({
    jooby(App()) {
        describe("swagger") {
            given("Pets API") {
                it("should get a swagger representation") {
                    //${'$'}ref
                    given()
                            .`when`()
                            .get("/swagger/swagger.json")
                            .then()
                            .assertThat()
                            .statusCode(Status.OK.value())
                            .extract()
                            .asString()
                            .let {
                                assertEquals("""{"swagger":"2.0","info":{"version":"v1","title":"Pets Kotlin API"},"basePath":"/","tags":[{"name":"ApiPet","description":"Everything about your Pets."}],"schemes":["http"],"consumes":["application/json"],"produces":["application/json"],"paths":{"/api/pet":{"get":{"tags":["ApiPet"],"summary":"List pets ordered by id","description":"","operationId":"getApiPetByStartMax","parameters":[{"name":"start","in":"query","description":"Start offset, useful for paging. Default is `0`.","required":false,"type":"integer","default":0,"format":"int32"},{"name":"max","in":"query","description":"Max page size, useful for paging. Default is `20`.","required":false,"type":"integer","default":20,"format":"int32"}],"responses":{"200":{"description":"Pets ordered by name.","schema":{"type":"array","items":{"${'$'}ref":"#/definitions/Pet"}}}}},"post":{"tags":["ApiPet"],"summary":"Add a new pet to the store","description":"","operationId":"postApiPet","parameters":[{"in":"body","name":"body","description":"Pet object that needs to be added to the store.","required":true,"schema":{"${'$'}ref":"#/definitions/Pet"}}],"responses":{"200":{"description":"Returns a saved pet.","schema":{"${'$'}ref":"#/definitions/Pet"}}}},"put":{"tags":["ApiPet"],"summary":"Update an existing pet","description":"","operationId":"putApiPet","parameters":[{"in":"body","name":"body","description":"Pet object that needs to be updated.","required":true,"schema":{"${'$'}ref":"#/definitions/Pet"}}],"responses":{"200":{"description":"Returns a saved pet.","schema":{"${'$'}ref":"#/definitions/Pet"}}}}},"/api/pet/{id}":{"get":{"tags":["ApiPet"],"description":"Find pet by ID","operationId":"getApiPetById","parameters":[{"name":"id","in":"path","description":"Pet ID.","required":true,"type":"integer","format":"int64"}],"responses":{"200":{"description":"Returns `200` with a single pet or `404`","schema":{"${'$'}ref":"#/definitions/Pet"}},"404":{"description":"Not Found"}}},"delete":{"tags":["ApiPet"],"summary":"Deletes a pet by ID","description":"","operationId":"deleteApiPet","parameters":[{"name":"id","in":"path","description":"Pet ID.","required":true,"type":"integer","format":"int64"}],"responses":{"204":{"description":"A `204`","schema":{"${'$'}ref":"#/definitions/Result"}}}}}},"definitions":{"Pet":{"type":"object","properties":{"id":{"type":"integer","format":"int64"},"name":{"type":"string"}}},"Result":{"type":"object"}}}""", it)
                            }
                }
            }
        }

        describe("raml") {
            given("Pets API") {
                it("should get a raml representation") {
                    given()
                            .`when`()
                            .get("/raml/api.raml")
                            .then()
                            .assertThat()
                            .statusCode(Status.OK.value())
                            .extract()
                            .asString()
                            .let {
                                assertEquals("""#%RAML 1.0
---
title: Pets Kotlin API
version: 0.0.0
baseUri: https://jooby-spec.herokuapp.com
mediaType:
- application/json
protocols:
- HTTP
types:
  Pet:
    type: object
    properties:
      id?: integer
      name?: string
    example:
      id: 0
      name: string
  Result:
    type: object
/api:
  /pet:
    description: Everything about your Pets.
    get:
      description: List pets ordered by id.
      queryParameters:
        start:
          required: false
          description: Start offset, useful for paging. Default is `0`.
          default: 0
          type: integer
        max:
          required: false
          description: Max page size, useful for paging. Default is `20`.
          default: 20
          type: integer
      responses:
        200:
          description: Pets ordered by name.
          body:
            application/json:
              type: Pet[]
    post:
      description: Add a new pet to the store.
      responses:
        200:
          description: Returns a saved pet.
          body:
            application/json:
              type: Pet
      body:
        type: Pet
    put:
      description: Update an existing pet.
      responses:
        200:
          description: Returns a saved pet.
          body:
            application/json:
              type: Pet
      body:
        type: Pet
    /{id}:
      description: Everything about your Pets.
      uriParameters:
        id:
          required: true
          description: Pet ID.
          type: integer
      get:
        description: Find pet by ID
        responses:
          200:
            description: Returns `200` with a single pet or `404`
            body:
              application/json:
                type: Pet
          404:
            description: Not Found
      delete:
        description: Deletes a pet by ID.
        responses:
          204:
            description: A `204`
            body:
              application/json:
                type: Result
""", it)
                            }
                }
            }
        }
    }
})