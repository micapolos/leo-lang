package leo

import leo.base.*

sealed class Term<out V>

data class MetaTerm<V>(
	val meta: Meta<V>) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class WordTerm<V>(
	val word: Word) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class StructureTerm<out V>(
	val structure: Structure<V>) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

// === constructors

val <V> Meta<V>.term: Term<V>
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

val <V> Field<V>.term: Term<V>
	get() =
		structure.term

val <V> Stack<Field<V>>.structureTerm: StructureTerm<V>
	get() =
		structure.structureTerm

val <V> Structure<V>.term: Term<V>
	get() =
		structureTerm

val <V> Structure<V>.structureTerm: StructureTerm<V>
	get() =
		StructureTerm(this)

val <V> Term<V>.itTerm: Term<V>
	get() =
		(itWord fieldTo this).term

fun <V> metaTerm(value: V) =
	value.meta.term

fun <V> term(field: Field<V>, vararg fields: Field<V>) =
	stack(field, *fields).structureTerm

val <V> StructureTerm<V>.fieldStack: Stack<Field<V>>
	get() =
		structure.fieldStack

fun <V> Term<V>.push(field: Field<V>): Term<V>? =
	structureTermOrNull?.structure?.plus(field)?.term

fun <V> Term<V>?.orNullPush(field: Field<V>): Term<V>? =
	if (this == null) field.onlyTerm
	else push(field)

val <V> Term<V>.topFieldOrNull: Field<V>?
	get() =
		structureTermOrNull?.topField

val <V> StructureTerm<V>.topField: Field<V>
	get() =
		structure.fieldStack.head

// === fields

val <V> Term<V>.fieldStreamOrNull: Stream<Field<V>>?
	get() =
		structureTermOrNull?.fieldStream

val <V> StructureTerm<V>.fieldStream: Stream<Field<V>>
	get() =
		structure.fieldStream

// === casting

val <V> Term<V>.metaTermOrNull
	get() =
		this as? MetaTerm

val <V> Term<V>.wordTermOrNull
	get() =
		this as? WordTerm

val <V> Term<V>.structureTermOrNull
	get() =
		this as? StructureTerm

val <V : Any> Term<V>.valueOrNull: V?
	get() =
		metaTermOrNull?.meta?.value

// === Appendable (pretty-print)

val <V> Term<V>.isSimple: Boolean
	get() =
		structureTermOrNull?.structure?.isSimple ?: true

fun <V> Appendable.append(term: Term<V>): Appendable =
	when (term) {
		is MetaTerm -> append(term.meta)
		is WordTerm -> append(term.word)
		is StructureTerm -> append(term.structure)
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
			is MetaTerm -> meta.token.onlyStream
			is WordTerm -> stream(word.token(), begin.control.token, end.control.token)
			is StructureTerm -> this.structure.tokenStream
		}

// === access

fun <V> StructureTerm<V>.valueStreamOrNull(key: Word): Stream<Term<V>>? =
	fieldStream.filterMap { field -> field.get(key) }

fun <V> StructureTerm<V>.onlyValueOrNull(key: Word): Term<V>? =
	valueStreamOrNull(key)?.onlyValueOrNull

val <V> Term<V>.onlyFieldOrNull: Field<V>?
	get() =
		structureTermOrNull?.structure?.fieldStack?.onlyOrNull

fun <V, R : Any> Term<V>.matchWord(fn: Word.() -> R?): R? =
	wordTermOrNull?.word?.let { fn(it) }

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
	structureTermOrNull?.matchAllFieldKeys(key, fn)

fun <V, R : Any> StructureTerm<V>.matchFirst(key: Word, fn: StructureTerm<V>?.(Term<V>) -> R?): R? =
	fieldStream.matchFirst({ it.key == key }) {
		fn(it.value)
	}

fun <V, R : Any> StructureTerm<V>.matchAllFieldKeys(key: Word, fn: (Term<V>) -> R?): Stream<R>? =
	fieldStream.filterMap { field ->
		if (field.key == key) fn(field.value)
		else null
	}

fun <V> Term<V>.select(key: Word): Term<V>? =
	structureTermOrNull?.select(key)

fun <V> StructureTerm<V>.select(key: Word): Term<V>? =
	if (isList)
		when (key) {
			lastWord -> fieldStack.head.value
			previousWord -> fieldStack.tail?.structureTerm
			else -> null
		}
	else valueStreamOrNull(key)?.run {
		nextOrNull.let { nextOrNull ->
			if (nextOrNull == null) first
			else map { value -> theWord fieldTo value }.stack.structureTerm
		}
	}


val StructureTerm<*>.isList: Boolean
	get() =
		true.fold(fieldStream) { field ->
			and(field.key == theWord)
		}

// === reflect

val Term<Nothing>.reflect: Field<Nothing>
	get() =
		termWord fieldTo this

fun <V> Term<V>.reflectMetaTerm(valueReflect: V.() -> Term<Nothing>): Term<Nothing> =
	when (this) {
		is MetaTerm -> meta.reflectMeta(valueReflect)
		is WordTerm -> word.term
		is StructureTerm -> structure.reflectMetaTerm(valueReflect)
	}

fun <V> Term<V>.reflectMeta(valueReflect: V.() -> Field<Nothing>): Term<Nothing> =
	reflectMetaTerm { valueReflect(this).onlyTerm }

val Field<Nothing>.parseTerm: Term<Nothing>?
	get() =
		matchKey(termWord) {
			parseTerm { null }
		}

fun <V> Term<Nothing>.parseTerm(parseValue: (Term<Nothing>) -> V?): Term<V>? =
	parseValue(this)?.meta?.term ?: when (this) {
		is MetaTerm -> fail
		is WordTerm -> word.term()
		is StructureTerm -> parseStructure(parseValue)?.term
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
		is MetaTerm -> meta.map(fn).term
		is WordTerm -> word.term()
		is StructureTerm -> this.structure.map(fn).term
	}
