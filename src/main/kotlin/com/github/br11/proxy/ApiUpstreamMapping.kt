package com.github.br11.proxy

import kotlin.reflect.KFunction2
import kotlin.reflect.KProperty1

interface ApiUpstreamMapping<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any> {

    fun mapping(vararg mappings: Pair<KProperty1<A, *>, KFunction2<D, *, *>>): ApiUpstreamMapping<A, B, C, D, E, An, Dn>

    fun getUpstreamResponse(): ApiProxy<A, B, C, D, E, An, Dn>
}