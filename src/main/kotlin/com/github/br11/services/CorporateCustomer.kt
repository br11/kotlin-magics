package com.github.br11.services

import java.math.BigDecimal

//data class CorporateCustomer(val ulid: String,
//                             val id: Long,
//                             val name: String,
//                             val shortName: String,
//                             val cnpj: String,
//                             val openingDate: String,
//                             val origin: Long,
//                             val productUlId: Long,
//                             val bankId: Integer,
//                             val bankBranchId: Integer,
//                             val accountNumber: String,
//                             val email: String,
//                             val paymentDay: Integer,
//                             val printedName: String,
//                             val accountUlId: Long,
//                             val proposalUlId: Long,
//                             val entryChannel: String,
//                             val score: Integer,
//                             val phones: List<String>,
//                             val addresses: List<String>,
//                             val partners: List<String>,
//                             val references: List<String>,
//                             val globalAmmount: BigDecimal,
//                             val maxAmount: BigDecimal,
//                             val limiteParcelas: BigDecimal)


data class CorporateCustomer(val ulidVal: String) {
    var ulid: String? = ulidVal
    var id: Long? = null
    var name: String? = null
    var shortName: String? = null
    var cnpj: String? = null
    var openingDate: String? = null
    var origin: Long? = null
    var productUlId: Long? = null
    var bankId: Integer? = null
    var bankBranchId: Integer? = null
    var accountNumber: String? = null
    var email: String? = null
    var paymentDay: Integer? = null
    var printedName: String? = null
    var accountUlId: Long? = null
    var proposalUlId: Long? = null
    var entryChannel: String? = null
    var score: Integer? = null
    var phones: List<PhoneNumber> =  listOf()
    var partners: List<Partner> = listOf()
    var references: List<String> = listOf()
    var globalAmmount: BigDecimal? = null
    var maxAmount: BigDecimal? = null
    var limiteParcelas: BigDecimal? = null
    var addresses : List<Address> = listOf()
}