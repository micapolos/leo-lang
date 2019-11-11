package leo14.typed.compiler

import leo.base.Empty
import leo14.typed.Typed

data class Resolver<T>(val empty: Empty)

fun <T> Resolver<T>.resolve(typed: Typed<T>): Typed<T> =
	TODO()