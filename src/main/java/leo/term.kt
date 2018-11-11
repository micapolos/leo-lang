package leo

import leo.base.*

sealed class Term<out V> {
	data class Meta<V>(
		val value: V) : Term<V>() {
		override fun toString() = appendableString { it.append(this) }
	}

	data class Identifier<V>(
		val word: Word) : Term<V>() {
		override fun toString() = appendableString { it.append(this) }
	}

	data class Structure<V>(
		val fieldStack: Stack<Field<V>>) : Term<V>() {
		override fun toString() = appendableString { it.append(this) }
	}
}

// === constructors

val <V> V.metaTerm
	get() =
		Term.Meta(this)

val <V> Stack<Field<V>>.term
	get() =
		Term.Structure(this)

// === helper constructors

fun <V> term(value: V) =
	Term.Meta(value)

fun <V> term(word: Word) =
	Term.Identifier<V>(word)

fun <V> term(field: Field<V>, vararg fields: Field<V>) =
	Term.Structure(stack(field, *fields))

// === pushing

fun <V> Term<V>?.push(word: Word): Term<V>? =
	when {
		this == null -> term(word)
		else -> null
	}

fun <V> Term<V>?.push(field: Field<V>): Term<V>? =
	when {
		this == null -> field.stack.term
		this is Term.Structure -> fieldStack.push(field).term
		else -> null
	}

// === casting

val <V> Term<V>.metaTermOrNull
	get() =
		this as? Term.Meta<V>

val <V> Term<V>.identifierTermOrNull
	get() =
		this as? Term.Identifier<V>

val <V> Term<V>.structureTermOrNull
	get() =
		this as? Term.Structure<V>

// === Appendable (core)

fun Appendable.appendCore(term: Term<*>): Appendable =
	when (term) {
		is Term.Meta -> appendString(term.value)
		is Term.Identifier -> appendCore(term)
		is Term.Structure -> appendCore(term)
	}

fun Appendable.appendCore(identifier: Term.Identifier<*>): Appendable =
	append(identifier.word)

fun Appendable.appendCore(script: Term.Structure<*>): Appendable =
	script
		.fieldStack
		.reverse
		.foldTop { field -> appendCore(field) }
		.andPop { appendable, field -> appendable.appendCore(field) }

// === Appendable (pretty-print)

val Term<*>.coreString
	get() =
		appendableString { it.appendCore(this) }

fun Appendable.append(term: Term<*>): Appendable =
	when (term) {
		is Term.Meta -> appendString(term.value)
		is Term.Identifier -> append(term)
		is Term.Structure -> append(term)
	}

fun Appendable.append(identifier: Term.Identifier<*>): Appendable =
	append(identifier.word)

fun Appendable.append(script: Term.Structure<*>): Appendable =
	script
		.fieldStack
		.reverse
		.foldTop { field -> append(field) }
		.andPop { appendable, field ->
			appendable
				.append(", ")
				.append(field)
		}

// === access

fun <V> Term<V>.all(key: Word): Stack<Term<V>>? =
	structureTermOrNull?.fieldStack?.filterMap { field ->
		field.termOrNull(key)
	}

fun <V> Term<V>.only(key: Word): Term<V>? =
	all(key)?.only

fun <V> Term<V>.singleton(key: Word): Term<V>? =
	when {
		this !is Term.Structure -> null
		fieldStack.pop != null -> null
		fieldStack.top.key != key -> null
		else -> fieldStack.top.value
	}

fun <V, R> Term<V>.match(key: Word, fn: (Term<V>) -> R): R? =
	when {
		this !is Term.Structure -> null
		fieldStack.top.key != key -> null
		fieldStack.pop != null -> null
		else -> fn(fieldStack.top.value)
	}

fun <V, R> Term<V>.match(firstKey: Word, secondKey: Word, fn: (Term<V>, Term<V>) -> R): R? =
	when {
		this !is Term.Structure -> null
		fieldStack.top.key != secondKey -> null
		fieldStack.pop?.top?.key != firstKey -> null
		fieldStack.pop.pop != null -> null
		else -> fn(fieldStack.pop.top.value, fieldStack.top.value)
	}

// === tokens

fun <V, R> Term<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
	when (this) {
		is Term.Meta -> this.foldTokens(folded, fn)
		is Term.Identifier -> this.foldTokens(folded, fn)
		is Term.Structure -> this.foldTokens(folded, fn)
	}

fun <V, R> Term.Meta<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
	fn(folded, token(value))

fun <V, R> Term.Identifier<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
	fn(folded, token(word))

fun <V, R> Term.Structure<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
	fieldStack
		.reverse
		.foldTop { field -> field.foldTokens(folded, fn) }
		.andPop { foldedTop, field -> field.foldTokens(foldedTop, fn) }

// === reflect

fun <V> Term<V>.fieldReflect(reflectValue: (V) -> Field<Nothing>): Field<Nothing> =
	reflect { value -> term(reflectValue(value)) }

fun <V> Term<V>.reflect(reflectValue: (V) -> Term<Nothing>): Field<Nothing> =
	termWord fieldTo term(termReflect(reflectValue))

fun <V> Term<V>.termReflect(reflectValue: (V) -> Term<Nothing>): Field<Nothing> =
	when (this) {
		is Term.Meta -> this.termReflect(reflectValue)
		is Term.Identifier -> this.termReflect()
		is Term.Structure -> this.termReflect(reflectValue)
	}

fun <V> Term.Meta<V>.termReflect(reflectValue: (V) -> Term<Nothing>): Field<Nothing> =
	metaWord fieldTo reflectValue(value)

fun <V> Term.Identifier<V>.termReflect(): Field<Nothing> =
	identifierWord fieldTo term(word.reflect)

fun <V> Term.Structure<V>.termReflect(reflectValue: (V) -> Term<Nothing>): Field<Nothing> =
	structureWord fieldTo fieldStack.reflect { field ->
		field.reflect(reflectValue)
	}

fun <V, R> Term<V>.map(fn: (V) -> R): Term<R> =
	when (this) {
		is Term.Meta -> Term.Meta(fn(value))
		is Term.Identifier -> Term.Identifier(word)
		is Term.Structure -> Term.Structure(fieldStack.map { field -> field.map(fn) })
	}

// === folding bytes

fun <V, R> R.foldBytes(term: Term<V>, metaFn: R.(V) -> R, fn: R.(Byte) -> R): R =
	when (term) {
		is Term.Meta -> metaFn(term.value)
		is Term.Identifier -> foldBytes(term.word, fn)
		is Term.Structure -> fold(term.fieldStack.reverse) { field -> foldBytes(field, metaFn, fn) }
	}

