package leo.lab

import leo.*
import leo.base.*

sealed class Term<out V> {
	data class Meta<V>(
		val type: Type<V>,
		val value: V) : Term<V>() {
		override fun toString() = appendableString { it.append(this) }

		data class Type<V>(
			val parseValue: (Term<Nothing>) -> V?,
			val reflectValue: (V) -> Term<Nothing>)
	}

	data class Node<V>(
		val lhsTermOrNull: Term<V>?,
		val word: Word,
		val rhsTermOrNull: Term<V>?) : Term<V>() {
		override fun toString() = appendableString { it.append(this) }
	}
}

// === constructors

val nothingTermType = Term.Meta.Type({ fail }, { fail })

fun <V> Term.Meta.Type<V>.term(value: V) =
	Term.Meta(this, value)

fun <V> nullTerm(): Term<V>? =
	null

val nullTerm: Term<Nothing>? =
	nullTerm()

fun <V> Term<V>?.plus(word: Word, rhsTermOrNull: Term<V>? = null): Term<V> =
	Term.Node(this, word, rhsTermOrNull)

// === casting

val <V> Term<V>.metaTermOrNull
	get() =
		this as? Term.Meta<V>

val <V> Term<V>.nodeTermTermOrNull
	get() =
		this as? Term.Node<V>

// === meta handling

val <V> Term.Meta<V>.reflect: Term<Nothing>
	get() =
		type.reflectValue(value)

// === Appendable (pretty-print)

val <V> Term<V>.isSimple: Boolean
	get() =
		when (this) {
			is Term.Meta -> this.reflect.isSimple
			is Term.Node -> this.lhsTermOrNull == null
		}

fun <V> Appendable.append(term: Term<V>): Appendable =
	when (term) {
		is Term.Meta -> append(term)
		is Term.Node -> append(term)
	}

fun <V> Appendable.append(metaTerm: Term.Meta<V>): Appendable =
	append(metaWord.fieldTo(term(metaTerm.type.reflectValue(metaTerm.value))))

fun <V> Appendable.append(nodeTerm: Term.Node<V>): Appendable =
	this
		.foldIfNotNull(nodeTerm.lhsTermOrNull) { lhs -> append(lhs).append(", ") }
		.append(nodeTerm.word.string)
		.foldIfNotNull(nodeTerm.rhsTermOrNull) { rhs ->
			if (rhs.isSimple) append(' ').append(rhs)
			else append('(').append(rhs).append(')')
		}

// === byte stream

val <V> Term<V>.coreString: String
	get() =
		appendableString { appendable ->
			appendable.fold(byteStream) { byte ->
				append(byte.toChar())
			}
		}

val <V> Term<V>.byteStream: Stream<Byte>
	get() =
		when (this) {
			is Term.Meta -> this.byteStream
			is Term.Node -> this.byteStream
		}

val <V> Term.Meta<V>.byteStream: Stream<Byte>
	get() =
		reflect.byteStream

val <V> Term.Node<V>.byteStream: Stream<Byte>
	get() =
		lhsTermOrNull?.byteStream
			.orNullThen(word.byteStream)
			.then('('.toByte().onlyStream)
			.thenIfNotNull(rhsTermOrNull?.byteStream)
			.then(')'.toByte().onlyStream)

//// === access
//
//fun <V> Term<V>.all(key: Word): Stack<Term<V>>? =
//	structureTermOrNull?.fieldStack?.filterMap { field ->
//		field.termOrNull(key)
//	}
//
//fun <V> Term<V>.only(key: Word): Term<V>? =
//	all(key)?.only
//
//fun <V> Term<V>.singleton(key: Word): Term<V>? =
//	when {
//		this !is Term.Node -> null
//		fieldStack.pop != null -> null
//		fieldStack.top.key != key -> null
//		else -> fieldStack.top.value
//	}
//
//val <V> Term<V>.onlyField: Field<V>?
//	get() =
//		structureTermOrNull?.fieldStack?.only
//
//fun <V, R> Term<V>.match(key: Word, fn: (Term<V>) -> R): R? =
//	when {
//		this !is Term.Node -> null
//		fieldStack.top.key != key -> null
//		fieldStack.pop != null -> null
//		else -> fn(fieldStack.top.value)
//	}
//
//fun <V, R> Term<V>.match(firstKey: Word, secondKey: Word, fn: (Term<V>, Term<V>) -> R): R? =
//	when {
//		this !is Term.Node -> null
//		fieldStack.top.key != secondKey -> null
//		fieldStack.pop?.top?.key != firstKey -> null
//		fieldStack.pop.pop != null -> null
//		else -> fn(fieldStack.pop.top.value, fieldStack.top.value)
//	}
//
//fun <V> Term<V>.select(key: Word): Term<V>? =
//	all(key)?.let { termStack ->
//		when {
//			termStack.pop == null -> termStack.top
//			else -> termStack.reverse.stream
//				.foldFirst { term ->
//					term(lastWord fieldTo term)
//				}
//				.foldNext { term ->
//					term(
//						previousWord fieldTo this,
//						lastWord fieldTo term)
//				}
//		}
//	}
//
//// === tokens
//
//fun <V, R> Term<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
//	when (this) {
//		is Term.Meta -> this.foldTokens(folded, fn)
//		is Term.Identifier -> this.foldTokens(folded, fn)
//		is Term.Node -> this.foldTokens(folded, fn)
//	}
//
//fun <V, R> Term.Meta<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
//	fn(folded, token(value))
//
//fun <V, R> Term.Identifier<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
//	fn(folded, token(word))
//
//fun <V, R> Term.Node<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
//	fieldStack
//		.reverse
//		.stream
//		.foldFirst { field -> field.foldTokens(folded, fn) }
//		.foldNext { field -> field.foldTokens(this, fn) }
//
//// === reflect
//
//fun <V> Term<V>.fieldReflect(reflectValue: (V) -> Field<Value>): Field<Value> =
//	reflect { value -> term(reflectValue(value)) }
//
//fun <V> Term<V>.reflect(reflectValue: (V) -> Term<Value>): Field<Value> =
//	termWord fieldTo term(termReflect(reflectValue))
//
//fun <V> Term<V>.termReflect(reflectValue: (V) -> Term<Value>): Field<Value> =
//	when (this) {
//		is Term.Meta -> this.termReflect(reflectValue)
//		is Term.Identifier -> this.termReflect()
//		is Term.Node -> this.termReflect(reflectValue)
//	}
//
//fun <V> Term.Meta<V>.termReflect(reflectValue: (V) -> Term<Value>): Field<Value> =
//	metaWord fieldTo reflectValue(value)
//
//fun <V> Term.Identifier<V>.termReflect(): Field<Value> =
//	identifierWord fieldTo term(word.reflect)
//
//fun <V> Term.Node<V>.termReflect(reflectValue: (V) -> Term<Value>): Field<Value> =
//	structureWord fieldTo fieldStack.reflect { field ->
//		field.reflect(reflectValue)
//	}
//
//fun <V, R> Term<V>.map(fn: (V) -> R): Term<R> =
//	when (this) {
//		is Term.Meta -> Term.Meta(fn(value))
//		is Term.Identifier -> Term.Identifier(word)
//		is Term.Node -> Term.Node(fieldStack.map { field -> field.map(fn) })
//	}
//
//fun <V, R> Term<V>.cast(): Term<R> =
//	map { fail }
//
//// === folding bytes
//
//fun <V> Term<V>.byteStream(metaByteStream: (V) -> Stream<Byte>): Stream<Byte> =
//	when (this) {
//		is Term.Meta -> metaByteStream(value)
//		is Term.Identifier -> word.byteStream
//		is Term.Node -> fieldStack.reverse.stream.map { it.byteStream(metaByteStream) }.join
//	}
//
//val Term<Value>.byteStream: Stream<Byte>
//	get() =
//		byteStream { fail }
