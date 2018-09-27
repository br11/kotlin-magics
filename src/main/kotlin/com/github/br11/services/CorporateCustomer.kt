package com.github.br11.services

import java.math.BigDecimal

data class CorporateCustomer(val ulidVal: String) {
    var ulid: String? = ulidVal
    var id: Long? = null
    var name: String? = null
    var shortName: String? = null
    var cnpj: String? = null
    var openingDate: String? = null
    var origin: Long? = null
    var productUlId: Long? = null
    var bankId: Int? = null
    var bankBranchId: Int? = null
    var accountNumber: String? = null
    var email: String? = null
    var paymentDay: Int? = null
    var printedName: String? = null
    var accountUlId: Long? = null
    var proposalUlId: Long? = null
    var entryChannel: String? = null
    var score: Int? = null
    var phones: List<PhoneNumber> = listOf()
    var partners: List<Partner> = listOf()
    var references: List<String> = listOf()
    var globalAmmount: BigDecimal? = null
    var maxAmount: BigDecimal? = null
    var limiteParcelas: BigDecimal? = null
    var addresses: List<Address> = listOf()

    constructor() : this("") {

    }
}