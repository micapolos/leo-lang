package leo

sealed class Scalar<out V>

data class MetaScalar<out V>(
	val meta: Meta<V>) : Scalar<V>()

data class WordScalar<out V>(
	val word: Word) : Scalar<V>()

val <V> Meta<V>.scalar: Scalar<V>
	get() =
		MetaScalar(this)

fun <V> Word.scalar(): Scalar<V> =
	WordScalar(this)

val Word.scalar: Scalar<Nothing>
	get() =
		scalar()

val <V> Scalar<V>.token
	get() =
		when (this) {
			is MetaScalar -> meta.value.metaToken
			is WordScalar -> word.token
		}

val <V> Scalar<V>.term: Term<V>
	get() =
		when (this) {
			is MetaScalar -> meta.value.metaTerm
			is WordScalar -> word.term
		}