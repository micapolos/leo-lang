package leo25

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.*
import leo14.Number

sealed class Value {
	override fun toString() = script.toString()
}

object EmptyValue : Value()
data class LinkValue(val link: Link) : Value() {
	override fun toString() = super.toString()
}

data class Native(val any: Any?)

sealed class Line
data class FunctionLine(val function: Function) : Line()
data class FieldLine(val field: Field) : Line()
data class NativeLine(val native: Native) : Line()

data class Link(val tail: Value, val head: Line)
data class Field(val name: String, val value: Value)

infix fun String.fieldTo(value: Value): Field = Field(this, value)
infix fun String.lineTo(value: Value): Line = FieldLine(this fieldTo value)
fun line(field: Field): Line = FieldLine(field)
fun line(literal: Literal): Line = literal.line
fun line(function: Function): Line = FunctionLine(function)
fun line(native: Native): Line = NativeLine(native)

fun native(any: Any?) = Native(any)
val Native.stringOrNull: String? get() = any as? String
val Native.numberOrNull: leo14.Number? get() = any as? leo14.Number

operator fun Value.plus(line: Line): Value = LinkValue(Link(this, line))
val emptyValue: Value get() = EmptyValue
fun value(vararg lines: Line) = emptyValue.fold(lines) { plus(it) }
fun value(name: String) = value(name lineTo value())

val Line.functionOrNull: Function? get() = (this as? FunctionLine)?.function
val Line.nativeOrNull: Native? get() = (this as? NativeLine)?.native
val Line.fieldOrNull: Field? get() = (this as? FieldLine)?.field

val Value.linkOrNull: Link? get() = (this as? LinkValue)?.link
val Value.resolveFunctionOrNull: Function? get() = linkOrNull?.onlyLineOrNull?.functionOrNull

val Link.onlyLineOrNull: Line?
	get() =
		if (tail is EmptyValue) head
		else null

val Link.onlyFieldOrNull: Field?
	get() =
		onlyLineOrNull?.fieldOrNull

val Value.resolve: Value
	get() =
		null
			?: resolveFunctionApplyOrNull
			?: resolveTextNameTextOrNull("append", String::plus)
			?: resolveNumberNameNumberOrNull("add", Number::plus)
			?: resolveNumberNameNumberOrNull("subtract", Number::minus)
			?: resolveNumberNameNumberOrNull("multiply", Number::times)
			?: resolveHashOrNull
			?: resolveEqualsOrNull
			?: resolveGetOrNull
			?: resolveMakeOrNull
			?: this

val Value.resolveFunctionApplyOrNull: Value?
	get() =
		linkOrNull
			?.run {
				tail.resolveFunctionOrNull?.let { function ->
					head.fieldOrNull?.valueOrNull(applyName)?.let { given ->
						function.apply(given)
					}
				}
			}

fun Value.resolveNumberNameNumberOrNull(name: String, fn: Number.(Number) -> Number): Value? =
	linkOrNull
		?.run {
			tail.numberOrNull?.let { lhs ->
				head.fieldOrNull?.valueOrNull(name)?.numberOrNull?.let { rhs ->
					value(line(literal(lhs.fn(rhs))))
				}
			}
		}

fun Value.resolveTextNameTextOrNull(name: String, fn: String.(String) -> String): Value? =
	linkOrNull
		?.run {
			tail.textOrNull?.let { lhs ->
				head.fieldOrNull?.valueOrNull(name)?.textOrNull?.let { rhs ->
					value(line(literal(lhs.fn(rhs))))
				}
			}
		}

val Value.resolveHashOrNull: Value?
	get() =
		resolvePostfixOrNull("hash") {
			value("hash" lineTo value(line(literal(hashCode()))))
		}

val Value.resolveEqualsOrNull: Value?
	get() =
		resolveInfixOrNull("equals") { rhs ->
			equals(rhs).value
		}

fun Value.selectOrNull(name: String): Value? =
	linkOrNull?.selectOrNull(name)

fun Link.selectOrNull(name: String): Value? =
	null
		?: head.selectOrNull(name)
		?: tail.selectOrNull(name)

val Line.selectName: String
	get() =
		when (this) {
			is FieldLine -> field.name
			is FunctionLine -> doingName
			is NativeLine -> nativeName
		}

val Line.selectValueOrNull: Value?
	get() =
		when (this) {
			is FieldLine -> field.value
			is FunctionLine -> null
			is NativeLine -> null
		}

val Literal.selectName: String
	get() =
		when (this) {
			is NumberLiteral -> numberName
			is StringLiteral -> textName
		}

fun Line.selectOrNull(name: String): Value? =
	notNullIf(name == selectName) { value(this) }

fun Value.getOrNull(name: String): Value? =
	linkOrNull?.onlyFieldOrNull?.value?.selectOrNull(name)

fun Field.valueOrNull(name: String): Value? =
	notNullIf(this.name == name) { value }

fun Value.make(name: String): Value =
	value(name lineTo this)

fun Value.resolve(name: String): Value =
	getOrNull(name) ?: make(name)

val Value.resolveGetOrNull: Value?
	get() =
		resolveInfixOrNull(getName) { rhs ->
			rhs.nameOrNull?.let { name ->
				getOrNull(name)
			}
		}

val Value.resolveMakeOrNull: Value?
	get() =
		resolveInfixOrNull(makeName) { rhs ->
			rhs.nameOrNull?.let { name ->
				make(name)
			}
		}

fun Value.plus(name: String): Value =
	plus(name lineTo value())

val Line.textOrNull: String?
	get() =
		fieldOrNull?.valueOrNull("text")?.linkOrNull?.onlyLineOrNull?.nativeOrNull?.any as? String

val Line.numberOrNull: Number?
	get() =
		fieldOrNull?.valueOrNull("number")?.linkOrNull?.onlyLineOrNull?.nativeOrNull?.any as? Number

val Value.textOrNull: String?
	get() =
		linkOrNull?.onlyLineOrNull?.textOrNull

val Value.numberOrNull: Number?
	get() =
		linkOrNull?.onlyLineOrNull?.numberOrNull

val Field.onlyNameOrNull: String?
	get() =
		notNullIf(value is EmptyValue) { name }

val Literal.native: Native
	get() =
		when (this) {
			is NumberLiteral -> native(number)
			is StringLiteral -> native(string)
		}

val Value.nameOrNull: String?
	get() =
		linkOrNull?.onlyLineOrNull?.fieldOrNull?.onlyNameOrNull

fun Value.resolveInfixOrNull(name: String, fn: Value.(Value) -> Value?): Value? =
	linkOrNull?.run {
		tail.let { lhs ->
			head.fieldOrNull?.valueOrNull(name)?.let { rhs ->
				lhs.fn(rhs)
			}
		}
	}

fun Value.resolvePostfixOrNull(name: String, fn: Value.() -> Value): Value? =
	resolveInfixOrNull(name) { rhs ->
		ifOrNull(rhs is EmptyValue) {
			fn()
		}
	}

val Boolean.value
	get() =
		value(booleanName lineTo value(if (this) trueName else falseName))

val Value.repeatValueOrNull: Value?
	get() =
		resolvePostfixOrNull(repeatName) { this }
