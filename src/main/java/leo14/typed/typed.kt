package leo14.typed

import leo14.lambda.Term
import leo14.lambda.id

data class Typed<out T>(val term: Term<T>, val type: Type)

infix fun <T> Term<T>.of(type: Type) = Typed(this, type)

fun <T> Typed<T>.plus(string: String, rhs: Typed<T>): Typed<T> = TODO()
fun <T> nullTyped(): Typed<T> = TODO()

fun <T> emptyTyped() = id<T>() of emptyType

val <T> Typed<T>.isEmpty get() = this == emptyTyped<T>()