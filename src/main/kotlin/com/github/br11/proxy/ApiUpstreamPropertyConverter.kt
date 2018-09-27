package com.github.br11.proxy

import kotlin.reflect.*

class ApiUpstreamPropertyConverter<A: Any, T: Any, R: Any, U: Any>(val prop: KFunction2<A, R, U>, val converter: KFunction1<T, R>) : KFunction2<A, R, U> {

    override val annotations: List<Annotation>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isAbstract: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isFinal: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isOpen: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val parameters: List<KParameter>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val returnType: KType
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val typeParameters: List<KTypeParameter>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val visibility: KVisibility?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun call(vararg args: Any?): U {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun callBy(args: Map<KParameter, Any?>): U {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isExternal: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isInfix: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isInline: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isOperator: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isSuspend: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun invoke(p1: A, p2: R): U {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toString(): String {
        return  "{$converter=$prop}"
    }
}

fun <A : Any, T : Any, R : Any, U: Any> convert(conversion: Pair<KFunction1<T, R>, KFunction2<A, R, U>>): KFunction2<A, R, U> {
    return ApiUpstreamPropertyConverter<A, T, R, U>(conversion.second, conversion.first)
}
