package leo

import leo.base.*

sealed class Term<out V>

data class MetaTerm<V>(
	val value: V) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class WordTerm<out V>(
	val word: Word) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class FieldsTerm<out V>(
	val fieldStack: Stack<Field<V>>) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

// === constructors

fun <V> metaTerm(value: V) =
	value.metaTerm

val <V> V.metaTerm
	get() =
		MetaTerm(this)

fun <V> Word.term(): Term<V> =
	WordTerm(this)

val Word.term: Term<Nothing>
	get() =
		term()

val <V> Field<V>.onlyTerm: Term<V>
	get() =
		term

val <V> Field<V>.term: FieldsTerm<V>
	get() =
		FieldsTerm(onlyStack)

val <V> Stack<Field<V>>.fieldsTerm
	get() =
		FieldsTerm(this)

val <V> Term<V>.itTerm: Term<V>
	get() =
		(itWord fieldTo this).term

fun <V> term(field: Field<V>, vararg fields: Field<V>) =
	stack(field, *fields).fieldsTerm

fun <V> FieldsTerm<V>?.fieldsPush(field: Field<V>): FieldsTerm<V> =
	this?.fieldStack.push(field).fieldsTerm

fun <V> Term<V>?.orNullPush(word: Word): Term<V> =
	word.term

fun <V> Term<V>.push(word: Word): Term<V> =
	word.fieldTo(this).onlyTerm

fun <V> Term<V>.push(field: Field<V>): Term<V>? =
	fieldsTermOrNull.let { fieldsTermOrNull ->
		if (fieldsTermOrNull != null) fieldsTermOrNull.fieldStack.push(field).fieldsTerm
		else itField.term.fieldsPush(field)
	}

fun <V> Term<V>?.orNullPush(field: Field<V>): Term<V>? =
	field.term

val <V> Term<V>.topFieldOrNull: Field<V>?
	get() =
		fieldsTermOrNull?.topField

val <V> FieldsTerm<V>.topField: Field<V>
	get() =
		fieldStack.top

// === fields

val <V> Term<V>.fieldStreamOrNull: Stream<Field<V>>?
	get() =
		fieldsTermOrNull?.fieldStream

val <V> FieldsTerm<V>.fieldStream: Stream<Field<V>>
	get() =
		fieldStack.reverse.stream

// === casting

val <V> Term<V>.metaTermOrNull
	get() =
		this as? MetaTerm

val <V> Term<V>.wordTermOrNull
	get() =
		this as? WordTerm

val <V> Term<V>.fieldsTermOrNull
	get() =
		this as? FieldsTerm

// === Appendable (pretty-print)

val <V> Term<V>.isSimple: Boolean
	get() =
		when (this) {
			is MetaTerm -> true
			is WordTerm -> true
			is FieldsTerm -> fieldStack.pop == null
		}

fun <V> Appendable.append(term: Term<V>): Appendable =
	when (term) {
		is MetaTerm -> append(term.value.string)
		is WordTerm -> append(term.word.string)
		is FieldsTerm -> term.fieldStream.let { fieldStream ->
			append(fieldStream.first).fold(fieldStream.nextOrNull) { field ->
				append(", ").append(field)
			}
		}
	}

// === stream

val Term<Nothing>.coreString: String
	get() =
		appendableString {
			it.fold(
				tokenStream.mapJoin(Token<Nothing>::characterStream).map(Character::char),
				Appendable::append)
		}

val Term<Nothing>.bitStream: Stream<Bit>
	get() =
		tokenStream
			.mapJoin(Token<Nothing>::characterStream)
			.mapJoin(Character::bitStream)

val <V> Term<V>.tokenStream: Stream<Token<V>>
	get() =
		when (this) {
			is MetaTerm -> value.metaToken.onlyStream
			is WordTerm -> word.token<V>().onlyStream
			is FieldsTerm -> this.fieldStream.mapJoin(Field<V>::tokenStream)
		}

// === access

fun <V> FieldsTerm<V>.valueStreamOrNull(key: Word): Stream<Term<V>>? =
	fieldStream.filterMap { field -> field.get(key) }

fun <V> FieldsTerm<V>.onlyValueOrNull(key: Word): Term<V>? =
	valueStreamOrNull(key)?.onlyValueOrNull

val <V> Term<V>.onlyFieldOrNull: Field<V>?
	get() =
		fieldsTermOrNull?.fieldStack?.run {
			pop.ifNull {
				top.key fieldTo top.value
			}
		}

fun <V, R : Any> Term<V>.matchWord(fn: Word.() -> R?): R? =
	wordTermOrNull?.let { fn(it.word) }

fun <V, R : Any> Term<V>.matchWord(key: Word, fn: () -> R?): R? =
	wordTermOrNull?.match(key, fn)

fun <V, R : Any> WordTerm<V>.match(key: Word, fn: () -> R?): R? =
	if (word == key) fn()
	else null

fun <V, R : Any> Term<V>.matchFieldKey(word: Word, fn: Term<V>.() -> R?): R? =
	fieldStreamOrNull?.matchFirst { field ->
		field.matchKey(word, fn)
	}

fun <V, R : Any> Term<V>.matchFieldKeys(key1: Word, key2: Word, fn: (Term<V>, Term<V>) -> R?): R? =
	fieldStreamOrNull?.matchFirst({ it.key == key1 }) { field1 ->
		this?.matchFirst({ it.key == key2 }) { field2 ->
			matchNull {
				fn(field1.value, field2.value)
			}
		}
	}

fun <V, R : Any> Term<V>.matchAllFieldKeys(key: Word, fn: (Term<V>) -> R?): Stream<R>? =
	fieldsTermOrNull?.matchAllFieldKeys(key, fn)

fun <V, R : Any> FieldsTerm<V>.matchFirst(key: Word, fn: FieldsTerm<V>?.(Term<V>) -> R?): R? =
	fieldStream.matchFirst({ it.key == key }) {
		fn(it.value)
	}

fun <V, R : Any> FieldsTerm<V>.matchAllFieldKeys(key: Word, fn: (Term<V>) -> R?): Stream<R>? =
	fieldStream.filterMap { field ->
		if (field.key == key) fn(field.value)
		else null
	}

fun <V> Term<V>.select(key: Word): Term<V>? =
	fieldsTermOrNull?.valueStreamOrNull(key)?.run {
		nextOrNull.let { nextOrNull ->
			if (nextOrNull == null) first
			else (lastWord fieldTo first).onlyTerm.fold(nextOrNull) { term ->
				term(
					previousWord fieldTo this,
					lastWord fieldTo term)
			}
		}
	}

// === reflect

val Term<Nothing>.reflect: Field<Nothing>
	get() = reflect { fail }

fun <V> Term<V>.reflect(metaValueReflect: V.() -> Field<Nothing>): Field<Nothing> =
	termWord fieldTo reflectMeta(metaValueReflect)

fun <V> Term<V>.reflectMeta(metaValueReflect: V.() -> Field<Nothing>): Term<Nothing> =
	when (this) {
		is MetaTerm -> term(metaWord fieldTo term(metaValueReflect(value)))
		is WordTerm -> word.term
		is FieldsTerm -> fieldStack.map { it.reflectMeta(metaValueReflect) }.fieldsTerm
	}

val Field<Nothing>.parseTerm: Term<Nothing>?
	get() =
		matchKey(termWord) {
			parseTermMeta { fail }
		}

fun <V> Term<Nothing>.parseTermMeta(parseMetaValue: (Field<Nothing>) -> V?): Term<V>? =
	matchFieldKey(metaWord) {
		onlyFieldOrNull?.let { onlyField ->
			parseMetaValue(onlyField)?.metaTerm
		}
	} ?: when (this) {
		is MetaTerm -> fail
		is WordTerm -> word.term
		is FieldsTerm -> fieldStack.mapOrNull { it.parseFieldMeta(parseMetaValue) }?.fieldsTerm
	}

// === select

val Term<Nothing>.evaluateSelect: Term<Nothing>
	get() =
		onlyFieldOrNull?.run {
			value.select(key)
		} ?: this

// === map

fun <V, R> Term<V>.map(fn: (V) -> R): Term<R> =
	when (this) {
		is MetaTerm -> fn(value).metaTerm
		is WordTerm -> word.term
		is FieldsTerm -> this.fieldStream.map { it.map(fn) }.stack.fieldsTerm
	}
