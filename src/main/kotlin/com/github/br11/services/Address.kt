package com.github.br11.services

import java.math.BigDecimal


data class Address(var street: String?) {
    var complement: String? = null
    var city: String? = null
    var zipcode: String? = null
    var state: String? = null
    var contry: String? = null
}