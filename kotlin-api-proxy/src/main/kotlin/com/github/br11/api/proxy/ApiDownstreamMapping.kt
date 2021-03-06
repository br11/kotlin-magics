package com.github.br11.api.proxy

import kotlin.reflect.KFunction1
import kotlin.reflect.KMutableProperty1

interface ApiDownstreamMapping<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any, Bn : Any, En : Any> {

    fun mapping(vararg mappings: Pair<KFunction1<E, *>, KMutableProperty1<B, *>>): ApiDownstreamMapping<A, B, C, D, E, An, Dn, Bn, En>

//    fun mappingNested(vararg mappings: Pair<KFunction1<*, *>, KMutableProperty1<*, *>>): ApiDownstreamMapping<A, B, C, D, E, An, Dn>
//
    fun onError(): ApiProxy<A, B, C, D, E, An, Dn, Bn, En>
}