package com.github.br11.kondoocthor.customer.domain

data class PhoneNumber(var idType: Long?) {
    var areaCode: String? = null
    var number: String? = null
    var extension: String? = null

    constructor() : this(0)
}