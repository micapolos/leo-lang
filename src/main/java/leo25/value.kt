package leo25

import leo.base.fold
import leo.base.notNullIf
import leo.base.nullOf

data class Word(val string: String)

sealed class Value
data class WordValue(val word: Word) : Value()
data class StringValue(val string: String) : Value()
data class StructValue(val struct: Struct) : Value()
data class FunctionValue(val function: Function) : Value()

data class Struct(val tail: Value?, val head: Field)
data class Field(val word: Word, val value: Value)

fun word(string: String) = Word(string)
infix fun Word.fieldTo(value: Value): Field = Field(this, value)
infix fun String.fieldTo(value: Value): Field = word(this) fieldTo value

operator fun Value?.plus(field: Field): Value =
	StructValue(Struct(this, field))

operator fun Value?.plus(pair: Pair<String, Value?>): Value =
	pair.let { (string, valueOrNull) ->
		Word(string).let { word ->
			when {
				valueOrNull != null -> StructValue(Struct(this, Field(word, valueOrNull)))
				this != null -> StructValue(Struct(null, Field(word, this)))
				else -> WordValue(word)
			}
		}
	}

fun value(word: Word): Value = WordValue(word)
fun value(string: String): Value = StringValue(string)
fun value(function: Function): Value = FunctionValue(function)
fun value(struct: Struct): Value = StructValue(struct)
fun value(pair: Pair<String, Value?>, vararg pairs: Pair<String, Value?>) =
	nullOf<Value>().plus(pair).fold(pairs) { plus(it) }

fun value(field: Field, vararg fields: Field) =
	nullOf<Value>().plus(field).fold(fields) { plus(it) }

val anyValue: Value = value("any" to null)
val Value.stringOrNull: String? get() = (this as? StringValue)?.string
val Value.structOrNull: Struct? get() = (this as? StructValue)?.struct
val Value.functionOrNull: Function? get() = (this as? FunctionValue)?.function

val Struct.onlyFieldOrNull: Field?
	get() =
		if (tail == null) head
		else null

val Value.resolve: Value
	get() =
		null
			?: resolveFunctionApplyOrNull
			?: resolveTextPlusTextOrNull
			?: resolveGetOrNull
			?: this

val Value.resolveGetOrNull: Value?
	get() =
		structOrNull?.onlyFieldOrNull?.let { field ->
			field.value.getOrNull(field.word.string)
		}

val Value.resolveFunctionApplyOrNull: Value?
	get() =
		structOrNull
			?.run {
				tail?.resolveFunctionOrNull?.let { function ->
					head.valueOrNull("apply")?.let { given ->
						function.apply(given)
					}
				}
			}

val Value.resolveTextPlusTextOrNull: Value?
	get() =
		structOrNull
			?.run {
				tail?.resolveStringOrNull?.let { lhs ->
					head.valueOrNull("plus")?.resolveStringOrNull?.let { rhs ->
						value("text" to value(lhs.plus(rhs)))
					}
				}
			}

fun Value.selectOrNull(name: String): Value? =
	structOrNull?.selectOrNull(name)

fun Struct.selectOrNull(name: String): Value? =
	null
		?: head.selectOrNull(name)
		?: tail?.selectOrNull(name)

fun Field.selectOrNull(name: String): Value? =
	notNullIf(word.string == name) { value(Struct(null, this)) }

fun Value.getOrNull(name: String): Value? =
	structOrNull?.onlyFieldOrNull?.value?.selectOrNull(name)

fun Field.valueOrNull(name: String): Value? =
	notNullIf(word.string == name) { value }

val Value.resolveFunctionOrNull: Function?
	get() =
		structOrNull?.onlyFieldOrNull?.valueOrNull("function")?.functionOrNull

val Value.resolveStringOrNull: String?
	get() =
		structOrNull?.onlyFieldOrNull?.valueOrNull("text")?.stringOrNull
