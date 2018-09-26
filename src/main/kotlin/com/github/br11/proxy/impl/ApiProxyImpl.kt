package com.github.br11.proxy.impl

import com.github.br11.proxy.ApiDownstreamMapping
import com.github.br11.proxy.ApiProxy
import com.github.br11.proxy.ApiUpstreamMapping
import com.github.br11.proxy.ApiUpstreamMappingNested
import kotlin.reflect.*
import kotlin.reflect.jvm.jvmErasure

open class ApiProxyImpl<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any> : ApiProxy<A, B, C, D, E, An, Dn>, ApiUpstreamMapping<A, B, C, D, E, An, Dn>, ApiDownstreamMapping<A, B, C, D, E, An, Dn>, ApiUpstreamMappingNested<A, B, C, D, E, An, Dn> {

    private var upstream: C? = null

    private var requestType: KClass<A>? = null
    private var responseType: KClass<B>? = null
    private var upstreamRequestType: KClass<D>? = null
    private var upstreamResponseType: KClass<E>? = null

    private var upstreamApi: KFunction2<C, D, E>? = null

    private var upstreamMappings: Map<KProperty<*>, KFunction2<*, *, Unit>> = hashMapOf()
    private var downstreamMappings: Map<KFunction1<*, *>, KMutableProperty1<*, *>> = hashMapOf()

    private var errorMappings: Map<KClass<Exception>, KClass<Exception>> = hashMapOf()

    private var nestedPropertyMapping: Pair<KMutableProperty1<A, List<An>>, KFunction2<D, MutableList<Dn>, *>>? = null

    constructor(init: ApiProxyImpl<A, B, C, D, E, An, Dn>.() -> Unit) {
        this.init()
    }

    constructor(upstream: C) {
        this.upstream = upstream
    }

    override fun <A1 : Any> receive(reqType: KClass<A1>): ApiProxy<A1, B, C, D, E, An, Dn> {
        requestType = reqType as KClass<A>
        return this as ApiProxy<A1, B, C, D, E, An, Dn>
    }

    override fun <C1 : Any, D1 : Any, E1 : Any> passTo(upstreamApi: KFunction2<C1, D1, E1>): ApiUpstreamMapping<A, B, C1, D1, E1, An, Dn> {
        this.upstreamApi = upstreamApi as KFunction2<C, D, E>
        this.upstreamRequestType = this.upstreamApi!!.parameters[0].type.jvmErasure as KClass<D>
        this.upstreamResponseType = this.upstreamApi!!.returnType.jvmErasure as KClass<E>

        return this as ApiUpstreamMapping<A, B, C1, D1, E1, An, Dn>
    }

    override fun <B1 : Any> respond(respType: KClass<B1>): ApiDownstreamMapping<A, B1, C, D, E, An, Dn> {
        responseType = respType as KClass<B>
        return this as ApiDownstreamMapping<A, B1, C, D, E, An, Dn>
    }

    override fun <T : Exception, X : Exception> map(errorMapping: Pair<KClass<T>, KClass<X>>) {
        errorMappings.plus(errorMapping)
    }

    override fun mapping(vararg mappings: Pair<KProperty1<A, *>, KFunction2<D, *, *>>): ApiUpstreamMapping<A, B, C, D, E, An, Dn> {
        upstreamMappings.plus(mappings)
        return this as ApiUpstreamMapping<A, B, C, D, E, An, Dn>
    }

    override fun <V : Any, Y : Any, Vn : Any, Yn : Any> mapNested(mapping: Pair<KMutableProperty1<V, Vn>, KFunction2<Y, Yn, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn> {
        return this as ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn>
    }

    override fun <V : Any, Y : Any, Vn : Any, Yn : Any> mapNestedList(mapping: Pair<KMutableProperty1<V, List<Vn>>, KFunction2<Y, MutableList<Yn>, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn> {
        return this as ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn>
    }

    override fun with(vararg mappings: Pair<KProperty1<An, *>, KFunction2<Dn, *, *>>): Pair<KMutableProperty1<A, List<An>>, KFunction2<D, MutableList<Dn>, *>> {
        upstreamMappings.plus(mappings)
        return nestedPropertyMapping!!
    }

    override fun mapping(vararg mappings: Pair<KFunction1<E, *>, KMutableProperty1<B, *>>): ApiDownstreamMapping<A, B, C, D, E, An, Dn> {
        downstreamMappings.plus(mappings)
        return this
    }

    override fun mappingNested(vararg mappings: Pair<KFunction1<*, *>, KMutableProperty1<*, *>>): ApiDownstreamMapping<A, B, C, D, E, An, Dn> {
        downstreamMappings.plus(mappings)
        return this
    }

    override fun getUpstreamResponse(): ApiProxy<A, B, C, D, E, An, Dn> {
        return this
    }

    override fun onError(): ApiProxy<A, B, C, D, E, An, Dn> {
        return this
    }

    override fun process(request: A): B {
        val upstreamRequest = upstreamRequestType!!.constructors.first().call()
        TODO("not implemented yet") // popular props

        val upstreamResponse = upstreamApi!!.invoke(upstream!!, upstreamRequest!!)

        val response = responseType!!.constructors.first().call()
        TODO("not implemented yet") // popular props

        return response
    }

    open fun <C : Any> use(upstream: C): ApiProxy<*, *, C, *, *, *, *> {
        return ApiProxyImpl<Any, Any, C, Any, Any, Any, Any>(upstream)
    }

}
