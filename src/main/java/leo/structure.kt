package leo

import leo.base.*

data class Structure<out V>(
	val fieldStack: Stack<Field<V>>) {
	override fun toString() = appendableString { it.append(this) }
}

val <V> Stack<Field<V>>.structure
	get() =
		Structure(this)

val <V> Field<V>.structure
	get() =
		onlyStack.structure

fun <V> structure(field: Field<V>, vararg fields: Field<V>): Structure<V> =
	stack(field, *fields).structure

val <V> Structure<V>.fieldStream: Stream<Field<V>>
	get() =
		fieldStack.reverse.stream

fun <V> Structure<V>.plus(field: Field<V>): Structure<V> =
	fieldStack.push(field).structure

// === util

val <V> Structure<V>.isSimple: Boolean
	get() = fieldStack.tail == null

val <V> Structure<V>.tokenStream: Stream<Token<V>>
	get() =
		fieldStream.mapJoin(Field<V>::tokenStream)

fun <V, R> Structure<V>.map(fn: (V) -> R): Structure<R> =
	fieldStack.map { field -> field.map(fn) }.structure

// === append

fun <V> Appendable.append(structure: Structure<V>): Appendable =
	structure.fieldStream.let { fieldStream ->
		append(fieldStream.first).fold(fieldStream.nextOrNull) { field ->
			append(", ").append(field)
		}
	}

// === reflect

val Structure<Nothing>.reflect: Field<Nothing>
	get() =
		structureWord fieldTo term

fun <V> Structure<V>.reflectMetaTerm(valueReflect: V.() -> Term<Nothing>): Term<Nothing> =
	fieldStack.map { it.reflectMetaTerm(valueReflect) }.structureTerm

fun <V> Structure<V>.reflectMeta(valueReflect: V.() -> Field<Nothing>): Term<Nothing> =
	reflectMetaTerm { valueReflect(this).onlyTerm }

// === parse

val Field<Nothing>.parseStructure: Structure<Nothing>?
	get() =
		matchKey(structureWord) {
			parseStructure { null }
		}

fun <V> Term<Nothing>.parseStructure(parseValue: (Term<Nothing>) -> V?): Structure<V>? =
	structureTermOrNull?.fieldStack?.mapOrNull { it.parseField(parseValue) }?.structure

