package com.github.br11.api.proxy.impl

import com.github.br11.api.proxy.*
import org.jetbrains.kotlin.backend.common.pop
import kotlin.reflect.*
import kotlin.reflect.jvm.jvmErasure

open class ApiProxyImpl<A : Any, B : Any, C : Any, D : Any, E : Any, An : Any, Dn : Any, Bn : Any, En : Any> : ApiProxy<A, B, C, D, E, An, Dn, Bn, En> {

    private var upstream: C? = null

    private var requestType: KClass<A>? = null
    private var responseType: KClass<B>? = null
    private var upstreamRequestType: KClass<D>? = null
    private var upstreamResponseType: KClass<E>? = null

    private var upstreamApi: KFunction2<C, D, E>? = null

    private var upstreamMappings: MutableMap<ApiPropertyPath<KProperty<*>>, ApiPropertyPath<KFunction2<*, *, *>>> = hashMapOf()
    private var downstreamMappings: MutableMap<ApiPropertyPath<KFunction1<*, *>>, ApiPropertyPath<KMutableProperty1<*, *>>> = hashMapOf()

    private var errorMappings: MutableMap<KClass<Exception>, KClass<Exception>> = hashMapOf()

    private var nestedUpstreamPropertyMapping: Pair<KMutableProperty1<A, List<An>>, KFunction2<D, MutableList<Dn>, *>>? = null
    private var nestedDownstreamPropertyMapping: Pair<KFunction1<E, MutableList<En>>, KMutableProperty1<B, List<Bn>>>? = null

    private val sourcePath: MutableList<String> = mutableListOf()
    private val targetPath: MutableList<String> = mutableListOf()

    private val apiUpstreamMapping = ApiUpstreamMappingImpl(this)
    private val apiDownstreamMapping = ApiDownstreamMappingImpl(this)

    constructor(init: ApiProxyImpl<A, B, C, D, E, An, Dn, Bn, En>.() -> Unit) {
        this.init()
    }

    fun use(upstream: C): ApiProxy<*, *, C, *, *, *, *, *, *> {
        this.upstream = upstream
        return this
    }

    override fun <A1 : Any> receive(reqType: KClass<A1>): ApiProxy<A1, B, C, D, E, An, Dn, Bn, En> {
        requestType = reqType as KClass<A>

        sourcePath.clear()
        sourcePath.add(requestType!!.simpleName!!)

        return this as ApiProxy<A1, B, C, D, E, An, Dn, Bn, En>
    }

    /*
     * Upstream
     */

    override fun <C1 : Any, D1 : Any, E1 : Any> passTo(upstreamApi: KFunction2<C1, D1, E1>): ApiUpstreamMapping<A, B, C1, D1, E1, An, Dn, Bn, En> {
        this.upstreamApi = upstreamApi as KFunction2<C, D, E>
        this.upstreamRequestType = this.upstreamApi!!.parameters[1].type.jvmErasure as KClass<D>
        this.upstreamResponseType = this.upstreamApi!!.returnType.jvmErasure as KClass<E>

        targetPath.clear()
        targetPath.add(upstreamRequestType!!.simpleName!!)

        return apiUpstreamMapping as ApiUpstreamMapping<A, B, C1, D1, E1, An, Dn, Bn, En>
    }

    override fun <V : Any, Y : Any, Vn : Any, Yn : Any> nested(mapping: Pair<KMutableProperty1<V, Vn>, KFunction2<Y, Yn, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn, *, *> {
        return apiUpstreamMapping.mapNested(mapping)
    }

    override fun <V : Any, Y : Any, Vn : Any, Yn : Any> nestedList(mapping: Pair<KMutableProperty1<V, List<Vn>>, KFunction2<Y, MutableList<Yn>, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn, *, *> {
        return apiUpstreamMapping.mapNestedList(mapping)
    }

    inner class ApiUpstreamMappingImpl(var proxy: ApiProxy<A, B, C, D, E, An, Dn, Bn, En>) : ApiUpstreamMapping<A, B, C, D, E, An, Dn, Bn, En>, ApiUpstreamMappingNested<A, B, C, D, E, An, Dn, Bn, En> {

        override fun mapping(vararg mappings: Pair<KProperty1<A, *>, KFunction2<D, *, *>>): ApiUpstreamMapping<A, B, C, D, E, An, Dn, Bn, En> {
            upstreamMappings.plusAssign(mappings.map { m ->
                Pair(ApiPropertyPath(sourcePath.joinToString("."), m.first as KProperty<*>),
                        ApiPropertyPath(targetPath.joinToString("."), m.second as KFunction2<*, *, *>))
            })

            return this as ApiUpstreamMapping<A, B, C, D, E, An, Dn, Bn, En>
        }

        override fun mapping(vararg mappings: Pair<KProperty1<An, *>, KFunction2<Dn, *, *>>): Pair<KMutableProperty1<A, List<An>>, KFunction2<D, MutableList<Dn>, *>> {
            upstreamMappings.plusAssign(mappings.map { m ->
                Pair(ApiPropertyPath(sourcePath.joinToString("."), m.first as KProperty<*>),
                        ApiPropertyPath(targetPath.joinToString("."), m.second as KFunction2<*, *, *>))
            })

            sourcePath.pop()
            targetPath.pop()

            return nestedUpstreamPropertyMapping!!
        }

        fun <V : Any, Y : Any, Vn : Any, Yn : Any> mapNested(mapping: Pair<KMutableProperty1<V, Vn>, KFunction2<Y, Yn, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn, *, *> {
            sourcePath.add(mapping.first.name)
            targetPath.add(mapping.second.name)

            return apiUpstreamMapping as ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn, *, *>
        }

        fun <V : Any, Y : Any, Vn : Any, Yn : Any> mapNestedList(mapping: Pair<KMutableProperty1<V, List<Vn>>, KFunction2<Y, MutableList<Yn>, *>>): ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn, *, *> {
            sourcePath.add(mapping.first.name)
            targetPath.add(mapping.second.name)

            nestedUpstreamPropertyMapping = mapping as Pair<KMutableProperty1<A, List<An>>, KFunction2<D, MutableList<Dn>, *>>
            return apiUpstreamMapping as ApiUpstreamMappingNested<V, *, *, Y, *, Vn, Yn, *, *>
        }

        override fun getUpstreamResponse(): ApiProxy<A, B, C, D, E, An, Dn, Bn, En> {
            sourcePath.clear()
            targetPath.add(upstreamResponseType!!.simpleName!!)

            return proxy
        }
    }

    /*
     * Downstream
     */

    override fun <B1 : Any> respond(respType: KClass<B1>): ApiDownstreamMapping<A, B1, C, D, E, An, Dn, Bn, En> {
        responseType = respType as KClass<B>

        sourcePath.clear()
        sourcePath.add(responseType!!.simpleName!!)

        return apiDownstreamMapping as ApiDownstreamMapping<A, B1, C, D, E, An, Dn, Bn, En>
    }

    override fun <W : Any, Z : Any, Wn : Any, Zn : Any> nested(mapping: Pair<KFunction1<Z, Zn>, KMutableProperty1<W, Wn>>): ApiDownstreamMappingNested<*, W, *, *, Z, *, *, Wn, Zn> {
        sourcePath.add(mapping.first.name)
        targetPath.add(mapping.second.name)

        return apiDownstreamMapping.mapNested(mapping)
    }

    override fun <W : Any, Z : Any, Wn : Any, Zn : Any> nestedList(mapping: Pair<KFunction1<Z, MutableList<Zn>>, KMutableProperty1<W, List<Wn>>>): ApiDownstreamMappingNested<*, W, *, *, Z, *, *, Wn, Zn> {
        sourcePath.add(mapping.first.name)
        targetPath.add(mapping.second.name)

        return apiDownstreamMapping.mapNestedList(mapping)
    }

    inner class ApiDownstreamMappingImpl(var proxy: ApiProxy<A, B, C, D, E, An, Dn, Bn, En>) : ApiDownstreamMapping<A, B, C, D, E, An, Dn, Bn, En>, ApiDownstreamMappingNested<A, B, C, D, E, An, Dn, Bn, En> {

        override fun mapping(vararg mappings: Pair<KFunction1<E, *>, KMutableProperty1<B, *>>): ApiDownstreamMapping<A, B, C, D, E, An, Dn, Bn, En> {
            downstreamMappings.plusAssign(mappings.map { m ->
                Pair(ApiPropertyPath(sourcePath.joinToString("."), m.first as KFunction1<*, *>),
                        ApiPropertyPath(targetPath.joinToString("."), m.second as KMutableProperty1<*, *>))
            })
            return this
        }

        override fun mapping(vararg mappings: Pair<KFunction1<En, *>, KMutableProperty1<Bn, *>>): Pair<KFunction1<E, MutableList<En>>, KMutableProperty1<B, List<Bn>>> {
            downstreamMappings.plusAssign(mappings.map { m ->
                Pair(ApiPropertyPath(sourcePath.joinToString("."), m.first as KFunction1<*, *>),
                        ApiPropertyPath(targetPath.joinToString("."), m.second as KMutableProperty1<*, *>))
            })

            sourcePath.pop()
            targetPath.pop()

            return nestedDownstreamPropertyMapping!!
        }

        fun <W : Any, Z : Any, Wn : Any, Zn : Any> mapNested(mapping: Pair<KFunction1<Z, Zn>, KMutableProperty1<W, Wn>>): ApiDownstreamMappingNested<*, W, *, *, Z, *, *, Wn, Zn> {
            sourcePath.add(mapping.first.name)
            targetPath.add(mapping.second.name)

            return this as ApiDownstreamMappingNested<*, W, *, *, Z, *, *, Wn, Zn>
        }

        fun <W : Any, Z : Any, Wn : Any, Zn : Any> mapNestedList(mapping: Pair<KFunction1<Z, MutableList<Zn>>, KMutableProperty1<W, List<Wn>>>): ApiDownstreamMappingNested<*, W, *, *, Z, *, *, Wn, Zn> {
            sourcePath.add(mapping.first.name)
            targetPath.add(mapping.second.name)

            nestedDownstreamPropertyMapping = mapping as Pair<KFunction1<E, MutableList<En>>, KMutableProperty1<B, List<Bn>>>
            return this as ApiDownstreamMappingNested<*, W, *, *, Z, *, *, Wn, Zn>
        }

        /* Error */
        override fun onError(): ApiProxy<A, B, C, D, E, An, Dn, Bn, En> {
            return this.proxy
        }
    }

    override fun <T : Exception, X : Exception> map(errorMapping: Pair<KClass<T>, KClass<X>>) {
        errorMappings.plus(errorMapping)
    }

    /*
     * Processing
     */

    override fun process(request: A): B {
        val upstreamRequest = upstreamRequestType!!.constructors.first().call()

        //TODO("not implemented yet") // popular props

        val upstreamResponse = upstreamApi!!.invoke(upstream!!, upstreamRequest)

        val response = responseType!!.constructors.first().call()

        //TODO("not implemented yet") // popular props

        return response
    }

}
