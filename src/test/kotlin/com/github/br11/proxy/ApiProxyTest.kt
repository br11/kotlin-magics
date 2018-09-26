package com.github.br11.proxy

import br.com.conductor.pier.api.v2.GlobaltagcadastroclienteApi
import br.com.conductor.pier.api.v2.invoker.ApiException
import br.com.conductor.pier.api.v2.model.PessoaJuridicaAprovadaPersist
import br.com.conductor.pier.api.v2.model.PessoaJuridicaAprovadaResponse
import com.github.br11.services.CorporateCustomer

class ApiProxyTest {

    fun shouldLookLikeThis() {

        val proxy = Proxy<CorporateCustomer, CorporateCustomer> {

            use(GlobaltagcadastroclienteApi())

                    .receive(CorporateCustomer::class)

                    .passTo(GlobaltagcadastroclienteApi::salvarPessoaJuridicaAprovadaUsingPOST)
                    .mapping(
                            CorporateCustomer::cnpj to PessoaJuridicaAprovadaPersist::setCnpj,
                            CorporateCustomer::name to PessoaJuridicaAprovadaPersist::setRazaoSocial,
                            CorporateCustomer::shortName to PessoaJuridicaAprovadaPersist::setNomeFantasia,
                            CorporateCustomer::productUlId to PessoaJuridicaAprovadaPersist::setIdProduto,
                            CorporateCustomer::email to PessoaJuridicaAprovadaPersist::setEmail,
                            CorporateCustomer::paymentDay to PessoaJuridicaAprovadaPersist::setDiaVencimento,
                            CorporateCustomer::accountNumber to PessoaJuridicaAprovadaPersist::setNumeroContaCorrente,
                            CorporateCustomer::bankBranchId to PessoaJuridicaAprovadaPersist::setNumeroAgencia,
                            CorporateCustomer::bankId to PessoaJuridicaAprovadaPersist::setNumeroBanco,
                            CorporateCustomer::maxAmount to PessoaJuridicaAprovadaPersist::setLimiteMaximo)

                    .getUpstreamResponse()

                    .respond(CorporateCustomer::class)
                    .mapping(
                            PessoaJuridicaAprovadaResponse::getId to convert(CorporateCustomer::ulid to ::toUlid),
                            PessoaJuridicaAprovadaResponse::getCnpj to CorporateCustomer::cnpj,
                            PessoaJuridicaAprovadaResponse::getRazaoSocial to CorporateCustomer::name,
                            PessoaJuridicaAprovadaResponse::getNomeFantasia to CorporateCustomer::shortName,
                            PessoaJuridicaAprovadaResponse::getIdProduto to CorporateCustomer::productUlId,
                            PessoaJuridicaAprovadaResponse::getEmail to CorporateCustomer::email,
                            PessoaJuridicaAprovadaResponse::getDiaVencimento to CorporateCustomer::paymentDay,
                            PessoaJuridicaAprovadaResponse::getNumeroContaCorrente to CorporateCustomer::accountNumber,
                            PessoaJuridicaAprovadaResponse::getNumeroAgencia to CorporateCustomer::bankBranchId,
                            PessoaJuridicaAprovadaResponse::getNumeroBanco to CorporateCustomer::bankId,
                            PessoaJuridicaAprovadaResponse::getLimiteMaximo to CorporateCustomer::maxAmount)

                    .onError()
                    .map(ApiException::class to RuntimeException::class)
        }


        val response = proxy.process(CorporateCustomer(""))

    }

    fun toUlid(id: Long): String {
        return ""
    }

    fun toId(ulid: String): Long {
        return 1L
    }

}