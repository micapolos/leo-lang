package leo.lab

import leo.Word

sealed class Term<out V> {
	data class Meta<V>(
		val value: V) : Term<V>() {
		//override fun toString() = appendableString { it.append(this) }
	}

	data class Node<V>(
		val lhsTermOrNull: Term<V>?,
		val word: Word,
		val rhsTermOrNull: Term<V>?) : Term<V>() {
		//override fun toString() = appendableString { it.append(this) }
	}
}

// === constructors

fun <V> nullTerm(): Term<V>? =
	null

val <V> V.metaTerm
	get() =
		Term.Meta(this)

fun <V> Term<V>?.plus(word: Word, rhsTermOrNull: Term<V>?): Term<V> =
	Term.Node(this, word, rhsTermOrNull)

// === casting

val <V> Term<V>.metaTermOrNull
	get() =
		this as? Term.Meta<V>

val <V> Term<V>.nodeTermTermOrNull
	get() =
		this as? Term.Node<V>

// === Appendable (core)

//fun Appendable.appendCore(term: Term<*>): Appendable =
//	when (term) {
//		is Term.Meta -> appendString(term.value)
//		is Term.Node -> appendCore(term)
//	}
//
//fun Appendable.appendCore(nodeTerm: Term.Node<*>): Appendable =
//	nodeTerm
//		.fieldStack
//		.reverse
//		.stream
//		.foldFirst { field -> appendCore(field) }
//		.foldNext { field -> appendCore(field) }
//
//// === Appendable (pretty-print)
//
//val Term<*>.coreString
//	get() =
//		appendableString { it.appendCore(this) }
//
//fun Appendable.append(term: Term<*>): Appendable =
//	when (term) {
//		is Term.Meta -> appendString(term.value)
//		is Term.Identifier -> append(term)
//		is Term.Node -> append(term)
//	}
//
//fun Appendable.append(identifier: Term.Identifier<*>): Appendable =
//	append(identifier.word)
//
//fun Appendable.append(nodeTerm: Term.Node<*>): Appendable =
//	nodeTerm
//		.fieldStack
//		.reverse
//		.stream
//		.foldFirst { field -> append(field) }
//		.foldNext { field -> append(", ").append(field) }
//
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
