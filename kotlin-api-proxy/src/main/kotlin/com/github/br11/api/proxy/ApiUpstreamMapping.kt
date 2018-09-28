package com.github.br11.api.proxy

import kotlin.reflect.KFunction2
import kotlin.reflect.KProperty1

interface ApiUpstreamMapping<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any, Bn : Any, En : Any> {

    fun mapping(vararg mappings: Pair<KProperty1<A, *>, KFunction2<D, *, *>>): ApiUpstreamMapping<A, B, C, D, E, An, Dn, Bn, En>

    fun getUpstreamResponse(): ApiProxy<A, B, C, D, E, An, Dn, Bn, En>
}