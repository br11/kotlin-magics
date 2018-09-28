package com.github.br11.api.proxy

import kotlin.reflect.KFunction1
import kotlin.reflect.KMutableProperty1

interface ApiDownstreamMappingNested<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any, Bn : Any, En : Any> {

    fun mapping(vararg mappings: Pair<KFunction1<En, *>, KMutableProperty1<Bn, *>>): Pair<KFunction1<E, MutableList<En>>, KMutableProperty1<B, List<Bn>>>

}