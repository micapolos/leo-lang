package leo14.typed

data class Compiled<T>(
	val context: Context<T>,
	val typed: Typed<T>)

fun <T> Compiled<T>.plus(typedLine: TypedLine<T>): Compiled<T> =
	TODO()