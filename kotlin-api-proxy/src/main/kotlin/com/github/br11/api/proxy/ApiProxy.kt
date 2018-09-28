package com.github.br11.api.proxy

import kotlin.reflect.KClass
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KMutableProperty1

interface ApiProxy<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any, Bn : Any, En : Any> {

    fun <A : Any> receive(reqType: KClass<A>): ApiProxy<A, B, C, D, E, An, Dn, Bn, En>

    fun <C : Any, D : Any, E : Any> passTo(upstreamApi: KFunction2<C, @ParameterName(name = "pessoaPersist") D, E>): ApiUpstreamMapping<A, B, C, D, E, An, Dn, Bn, En>

    fun <B : Any> respond(respType: KClass<B>): ApiDownstreamMapping<A, B, C, D, E, An, Dn, Bn, En>

    fun <T : Exception, X : Exception> map(errorMapping: Pair<KClass<T>, KClass<X>>)

    fun <V : Any, Y : Any, Vn : Any, Yn : Any> nested(mapping: Pair<KMutableProperty1<V, Vn>, KFunction2<Y, Yn, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn, *, *>

    fun <V : Any, Y : Any, Vn : Any, Yn : Any> nestedList(mapping: Pair<KMutableProperty1<V, List<Vn>>, KFunction2<Y, MutableList<Yn>, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn, *, *>

    fun <W : Any, Z : Any, Wn : Any, Zn : Any> nested(mapping: Pair<KFunction1<Z, Zn>, KMutableProperty1<W, Wn>>): ApiDownstreamMappingNested<*, W, *, *, Z, *, *, Wn, Zn>

    fun <W : Any, Z : Any, Wn : Any, Zn : Any> nestedList(mapping: Pair<KFunction1<Z, MutableList<Zn>>, KMutableProperty1<W, List<Wn>>>): ApiDownstreamMappingNested<*, W, *, *, Z, *, *, Wn, Zn>

    fun process(request: A): B
}