package com.github.br11.services

import br.com.conductor.pier.api.v2.GlobaltagcadastroclienteApi
import br.com.conductor.pier.api.v2.invoker.ApiException
import br.com.conductor.pier.api.v2.model.*
import com.github.br11.api.com.github.br11.proxy.conversions.idToUlid
import com.github.br11.api.com.github.br11.proxy.conversions.ulidToId
import com.github.br11.proxy.Proxy
import com.github.br11.proxy.convert

class NewCorporateCustomerService {

    val proxy: Proxy<CorporateCustomer, CorporateCustomer>

    constructor() {

        proxy = Proxy {

            use(GlobaltagcadastroclienteApi())

                    .receive(CorporateCustomer::class)

                    .passTo(GlobaltagcadastroclienteApi::salvarPessoaJuridicaAprovadaUsingPOST)
                    .mapping(

                            CorporateCustomer::cnpj to PessoaJuridicaAprovadaPersist::setCnpj,
                            CorporateCustomer::name to PessoaJuridicaAprovadaPersist::setRazaoSocial,
                            CorporateCustomer::shortName to PessoaJuridicaAprovadaPersist::setNomeFantasia,
                            CorporateCustomer::productUlId to convert(::ulidToId to PessoaJuridicaAprovadaPersist::setIdProduto),
                            CorporateCustomer::email to PessoaJuridicaAprovadaPersist::setEmail,
                            CorporateCustomer::paymentDay to PessoaJuridicaAprovadaPersist::setDiaVencimento,
                            CorporateCustomer::accountNumber to PessoaJuridicaAprovadaPersist::setNumeroContaCorrente,
                            CorporateCustomer::bankBranchId to PessoaJuridicaAprovadaPersist::setNumeroAgencia,
                            CorporateCustomer::bankId to PessoaJuridicaAprovadaPersist::setNumeroBanco,
                            CorporateCustomer::maxAmount to PessoaJuridicaAprovadaPersist::setLimiteMaximo,

                            nestedList(CorporateCustomer::addresses to PessoaJuridicaAprovadaPersist::setEnderecos)
                                    .mapping(Address::street to EnderecoAprovadoPersistValue::setLogradouro,
                                            Address::complement to EnderecoAprovadoPersistValue::setComplemento,
                                            Address::city to EnderecoAprovadoPersistValue::setCidade,
                                            Address::state to EnderecoAprovadoPersistValue::setUf,
                                            Address::zipcode to EnderecoAprovadoPersistValue::setCep,
                                            Address::contry to EnderecoAprovadoPersistValue::setPais),

                            nestedList(CorporateCustomer::partners to PessoaJuridicaAprovadaPersist::setSocios)
                                    .mapping(Partner::name to SocioAprovadoPersistValue::setNome,
                                            Partner::cpf to SocioAprovadoPersistValue::setCpf,
                                            Partner::birthDate to SocioAprovadoPersistValue::setDataNascimento,
                                            Partner::occupation to SocioAprovadoPersistValue::setProfissao,
                                            Partner::email to SocioAprovadoPersistValue::setEmail,

                                            nestedList(Partner::phoneNumbers to SocioAprovadoPersistValue::setTelefones)
                                                    .mapping(PhoneNumber::idType to TelefonePessoaAprovadaPersistValue::setIdTipoTelefone,
                                                            PhoneNumber::areaCode to TelefonePessoaAprovadaPersistValue::setDdd,
                                                            PhoneNumber::number to TelefonePessoaAprovadaPersistValue::setTelefone,
                                                            PhoneNumber::extension to TelefonePessoaAprovadaPersistValue::setRamal)))

                    .getUpstreamResponse()

                    .respond(CorporateCustomer::class)
                    .mapping(

                            PessoaJuridicaAprovadaResponse::getCnpj to CorporateCustomer::cnpj,
                            PessoaJuridicaAprovadaResponse::getId to convert(::idToUlid to CorporateCustomer::ulid),
                            PessoaJuridicaAprovadaResponse::getRazaoSocial to CorporateCustomer::name,
                            PessoaJuridicaAprovadaResponse::getNomeFantasia to CorporateCustomer::shortName,
                            PessoaJuridicaAprovadaResponse::getIdProduto to CorporateCustomer::productUlId,
                            PessoaJuridicaAprovadaResponse::getEmail to CorporateCustomer::email,
                            PessoaJuridicaAprovadaResponse::getDiaVencimento to CorporateCustomer::paymentDay,
                            PessoaJuridicaAprovadaResponse::getNumeroContaCorrente to CorporateCustomer::accountNumber,
                            PessoaJuridicaAprovadaResponse::getNumeroAgencia to CorporateCustomer::bankBranchId,
                            PessoaJuridicaAprovadaResponse::getNumeroBanco to CorporateCustomer::bankId,
                            PessoaJuridicaAprovadaResponse::getLimiteMaximo to CorporateCustomer::maxAmount,

                            nestedList(PessoaJuridicaAprovadaResponse::getEnderecos to CorporateCustomer::addresses)
                                    .mapping(EnderecoAprovadoResponse::getLogradouro to Address::street,
                                            EnderecoAprovadoResponse::getComplemento to Address::complement,
                                            EnderecoAprovadoResponse::getCidade to Address::city,
                                            EnderecoAprovadoResponse::getUf to Address::state,
                                            EnderecoAprovadoResponse::getCep to Address::zipcode),

                            nestedList(PessoaJuridicaAprovadaResponse::getSocios to CorporateCustomer::partners)
                                    .mapping(SocioAprovadoResponse::getNome to Partner::name,
                                            SocioAprovadoResponse::getCpf to Partner::cpf,
                                            SocioAprovadoResponse::getDataNascimento to Partner::birthDate,
                                            SocioAprovadoResponse::getProfissao to Partner::occupation,
                                            SocioAprovadoResponse::getEmail to Partner::email,

                                            nestedList(SocioAprovadoResponse::getTelefones to Partner::phoneNumbers)
                                                    .mapping(TelefonePessoaAprovadaResponse::getIdTipoTelefone to PhoneNumber::idType,
                                                            TelefonePessoaAprovadaResponse::getDdd to PhoneNumber::areaCode,
                                                            TelefonePessoaAprovadaResponse::getTelefone to PhoneNumber::number,
                                                            TelefonePessoaAprovadaResponse::getRamal to PhoneNumber::extension)))

                    .onError()
                    .map(ApiException::class to RuntimeException::class)
        }
    }

    fun execute(request: CorporateCustomer): CorporateCustomer {
        return proxy.process(request)
    }
}

fun main(args: Array<String>) {
    val service = NewCorporateCustomerService()
    val request = CorporateCustomer("askjasiupijqljwelkjqwe")

    val response = service.execute(request)

    println(response)
}