package com.github.br11.services


data class PhoneNumber(var idType: Long?) {
    var areaCode: String? = null
    var number: String? = null
    var extension: String? = null
}