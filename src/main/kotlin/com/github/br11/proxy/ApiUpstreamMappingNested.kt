package com.github.br11.proxy

import kotlin.reflect.KFunction2
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

interface ApiUpstreamMappingNested<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any> {

    fun with(vararg mappings: Pair<KProperty1<An, *>, KFunction2<Dn, *, *>>): Pair<KMutableProperty1<A, List<An>>, KFunction2<D, MutableList<Dn>, *>>

    fun getUpstreamResponse(): ApiProxy<A, B, C, D, E, An, Dn>

}