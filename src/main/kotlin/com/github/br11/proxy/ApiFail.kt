package com.github.br11.proxy

import kotlin.reflect.KClass

interface ApiFail<T : Any> {

    fun handle(handler: (T) -> Any)

}