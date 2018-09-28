package com.github.br11.api.proxy

import com.github.br11.api.proxy.impl.ApiProxyImpl

open class Proxy<A : Any, B : Any>(init: ApiProxyImpl<A, B, Any, Any, Any, Any, Any, Any, Any>.() -> Unit) : ApiProxyImpl<A, B, Any, Any, Any, Any, Any, Any, Any>(init) {

}
