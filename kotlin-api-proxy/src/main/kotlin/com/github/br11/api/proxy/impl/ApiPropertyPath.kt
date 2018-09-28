package com.github.br11.api.proxy.impl

data class ApiPropertyPath<P : Any>(val path: String, val property: P) {

    override fun toString(): String {
        return "$path"
    }
}