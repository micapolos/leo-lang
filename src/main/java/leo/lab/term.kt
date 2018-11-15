package leo.lab

import leo.Word
import leo.base.*
import leo.termWord

data class Term<out V>(
	val fieldStack: Stack<Field<V>>) {
	override fun toString() = appendableString { it.append(this) }
}

// === constructors

val <V> Stack<Field<V>>.term
	get() =
		Term(this)

fun <V> term(field: Field<V>, vararg fields: Field<V>) =
	stack(field, *fields).term

val Word.term: Term<Unit>
	get() =
		term(field)

val <V> Field<V>.term: Term<V>
	get() =
		onlyStack.term

// === Appendable (pretty-print)

val <V> Term<V>.isSimple: Boolean
	get() =
		fieldStack.pop == null

fun <V> Appendable.append(term: Term<V>): Appendable =
	term.fieldStack.reverse.stream
		.foldFirst { field -> append(field) }
		.foldNext { field -> append(", ").append(field) }

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
		fieldStack.reverse.stream
			.foldFirst { field -> field.byteStream }
			.foldNext { field -> then(field.byteStream) }

// === access

//fun <V> Term<V>.all(key: Word): Stack<Term<V>>? =
//	fieldStack?.filterMap { field ->
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

// === reflect

val <V> Term<V>.reflect: Field<Unit>
	get() =
		termWord fieldTo fieldStack.reflect(Field<V>::reflect)

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
