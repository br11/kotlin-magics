package com.github.br11.proxy

import com.github.br11.proxy.impl.ApiProxyImpl

open class Proxy<A : Any, B : Any>(init: ApiProxyImpl<A, B, Any, Any, Any, Any, Any>.() -> Unit) : ApiProxyImpl<A, B, Any, Any, Any, Any, Any>(init) {

    override fun <C : Any> use(upstream: C): ApiProxy<*, *, C, *, *, *, *> {
        return ApiProxyImpl<Any, Any, C, Any, Any, Any, Any>(upstream)
    }

}
