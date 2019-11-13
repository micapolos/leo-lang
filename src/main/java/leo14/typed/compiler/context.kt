package leo14.typed.compiler

import leo14.Literal
import leo14.any

data class Context<T>(val lit: Literal.() -> T)

val anyContext: Context<Any> = Context { any }

fun <T> Context<T>.compile(literal: Literal): T =
	literal.lit()