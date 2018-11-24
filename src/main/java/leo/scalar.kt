package leo

import leo.base.*

sealed class Scalar<out V>

data class MetaScalar<out V>(
	val meta: Meta<V>) : Scalar<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class WordScalar<out V>(
	val word: Word) : Scalar<V>() {
	override fun toString() = appendableString { it.append(this) }
}

val <V> Meta<V>.scalar: Scalar<V>
	get() =
		MetaScalar(this)

fun <V> Word.scalar(): Scalar<V> =
	WordScalar(this)

val Word.scalar: Scalar<Nothing>
	get() =
		scalar()

val <V> Scalar<V>.term: Term<V>
	get() =
		when (this) {
			is MetaScalar -> meta.value.metaTerm
			is WordScalar -> word.term
		}

// === token stream

val <V> Scalar<V>.tokenStream: Stream<Token<V>>
  get() =
	  when (this) {
		  is MetaScalar -> meta.value.metaToken.onlyStream
		  is WordScalar -> stream(word.token(), begin.token, end.token)
	  }

// === appendable

fun <V> Appendable.append(scalar: Scalar<V>): Appendable =
	when (scalar) {
		is MetaScalar -> append(scalar.meta)
		is WordScalar -> append(scalar.word).append(beginString).append(endString)
	}

// === reflect

fun <V> Scalar<V>.reflect(metaValueReflect: V.() -> Field<Nothing>): Field<Nothing> =
	scalarWord fieldTo term(
		when (this) {
			is MetaScalar -> meta.reflect(metaValueReflect)
			is WordScalar -> word.reflect
		})

val Scalar<Nothing>.reflect: Field<Nothing>
	get() =
		reflect { fail }

// === parse

val Field<Nothing>.parseScalar: Scalar<Nothing>?
  get() =
	  matchKey(scalarWord) {
		  onlyFieldOrNull?.parseWord?.scalar
	  }