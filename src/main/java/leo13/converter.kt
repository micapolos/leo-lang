package leo13

interface Converter<A, B> : Scripting {
	fun convert(value: A): Processor<B>
}

fun <A, B> errorConverter(): Converter<A, B> =
	error.converter { tracedError() }
