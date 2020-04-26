package leo15.lambda.runtime.type

import leo15.lambda.runtime.builder.Term

data class Typed<V, T>(val term: Term<V>, val type: Type<T>)

infix fun <V, T> Term<V>.of(type: Type<T>) = Typed(this, type)

