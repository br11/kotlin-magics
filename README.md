# kotlin-magics
## Kotlin Advanced API Swagger Reflection 

### Basics
#### build
./gradlew clean build install -x test

#### Running

> com.github.br11.kondoocthor.customer.api.CustomerApi::main

```
[main] INFO com.github.br11.kondoocthor.customer.api.CustomerApi - [dev@netty]: Server started in 1266ms

  GET    /swagger/swagger.json    [*/*]     [*/*]    (/anonymous)
  GET    /swagger/swagger.yml     [*/*]     [*/*]    (/anonymous)
  GET    /swagger/static/**       [*/*]     [*/*]    (/anonymous)
  GET    /swagger/static/**       [*/*]     [*/*]    (/anonymous)
  GET    /swagger                 [*/*]     [*/*]    (/anonymous)
  GET    /raml/api.raml           [*/*]     [*/*]    (/anonymous)
  GET    /raml/static/**          [*/*]     [*/*]    (/anonymous)
  GET    /raml                    [*/*]     [*/*]    (/anonymous)
  GET    /                        [*/*]     [*/*]    (/anonymous)
  GET    /api/customer            [*/*]     [*/*]    (/anonymous)
  GET    /api/customer/:ulid      [*/*]     [*/*]    (/anonymous)
  POST   /api/customer            [*/*]     [*/*]    (/anonymous)
  PUT    /api/customer/:ulid      [*/*]     [*/*]    (/anonymous)
  DELETE /api/customer/:ulid      [*/*]     [*/*]    (/anonymous)

listening on:
  http://localhost:8080/
```

### More

#### Documenting API with javadoc - Magic with Kooby & Swagger

Source code
````kotlin
class CustomerApi : Kooby({

    /** JSON: */
    use(Jackson())

    /** Export API to Swagger and RAML: */
    use(ApiTool()
            .filter { r -> r.pattern().startsWith("/api") }
            .swagger()
            .raml())
            
    path("/api/customer") {
    
        /**
         * Find CorporateCustomer by ULID
         *
         * @param ulid CorporateCustomer ULID.
         * @return Returns `200` with a single CorporateCustomer or `404`
         */
        get("/:ulid") {
            CorporateCustomerService().retrieveApproved(param<String>("ulid"))
        }
    
        /**
         * Add a new CorporateCustomer to the database.
         *
         * @param body CorporateCustomer object that needs to be added to the database.
         * @return Returns a saved CorporateCustomer.
         */
        post {
             CorporateCustomerService().saveApproved(body<CorporateCustomer>())
        }
        
        /* to be continued */
````

This is the output
.
![](./apidoc.png)

#### Fluent Interface for API Proxing - Uhu!!! a lot of relections :P
````kotlin
class CorporateCustomerService {

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

    fun saveApproved(request: CorporateCustomer): CorporateCustomer {
        return proxy.process(request)
    }
}
````
