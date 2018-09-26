package com.github.br11.services

import java.sql.Date


data class Partner(var name: String?) {
    var cpf: String? = null
    var birthDate: Date? = null
    var occupation: String? = null
    var email: String? = null
    var phoneNumbers: List<PhoneNumber> = listOf()
}