package com.github.br11.api.proxy

import kotlin.reflect.KClass

interface ApiFail<T : Any> {

    fun handle(handler: (T) -> Any)

}