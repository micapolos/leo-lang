package leo13

import leo9.stack

interface Converter<A, B> : Scripting {
	fun convert(value: A): B
}

fun <S : Scripting, A, B> S.errorConverter(fn: S.(A) -> B): Converter<A, B> =
	converter { tracedError() }

fun <A : Scripting, B> errorConverter(): Converter<A, B> =
	stack<A>().scripting.converter { tracedError() }
