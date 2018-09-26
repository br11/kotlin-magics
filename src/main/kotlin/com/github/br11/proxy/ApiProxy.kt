package com.github.br11.proxy

import kotlin.reflect.KClass
import kotlin.reflect.KFunction2
import kotlin.reflect.KMutableProperty1

interface ApiProxy<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any> {

    fun <A : Any> receive(reqType: KClass<A>): ApiProxy<A, B, C, D, E, An, Dn>

    fun <C : Any, D : Any, E : Any> passTo(upstreamApi: KFunction2<C, @ParameterName(name = "pessoaPersist") D, E>): ApiUpstreamMapping<A, B, C, D, E, An, Dn>

    fun <B : Any> respond(respType: KClass<B>): ApiDownstreamMapping<A, B, C, D, E, An, Dn>

    fun <T : Exception, X : Exception> map(errorMapping: Pair<KClass<T>, KClass<X>>)

    fun <V : Any, Y : Any, Vn : Any, Yn : Any> mapNested(mapping: Pair<KMutableProperty1<V, Vn>, KFunction2<Y, Yn, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn>

    fun <V : Any, Y : Any, Vn : Any, Yn : Any> mapNestedList(mapping: Pair<KMutableProperty1<V, List<Vn>>, KFunction2<Y, MutableList<Yn>, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn>

    fun process(request: A): B
}