package leo25

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo.base.orNullIf
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

sealed class Rhs
data class ValueRhs(val value: Value) : Rhs()
data class FunctionRhs(val function: Function) : Rhs()
data class NativeRhs(val native: Native) : Rhs()

fun rhs(value: Value): Rhs = ValueRhs(value)
fun rhs(function: Function): Rhs = FunctionRhs(function)
fun rhs(native: Native): Rhs = NativeRhs(native)

val Rhs.valueOrNull: Value? get() = (this as? ValueRhs)?.value
val Rhs.functionOrNull: Function? get() = (this as? FunctionRhs)?.function
val Rhs.nativeRhs: Native? get() = (this as? NativeRhs)?.native

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
val Native.numberOrNull: Number? get() = any as? Number

operator fun Value.plus(line: Line): Value = LinkValue(Link(this, line))
val emptyValue: Value get() = EmptyValue
fun value(vararg lines: Line) = emptyValue.fold(lines) { plus(it) }
fun value(name: String) = value(name lineTo value())

val Line.functionOrNull: Function? get() = (this as? FunctionLine)?.function
val Line.nativeOrNull: Native? get() = (this as? NativeLine)?.native
val Line.fieldOrNull: Field? get() = (this as? FieldLine)?.field

val Value.linkOrNull: Link? get() = (this as? LinkValue)?.link
val Value.functionOrNull: Function? get() = lineOrNull?.functionOrNull
val Value.lineOrNull: Line? get() = linkOrNull?.onlyLineOrNull

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
			?: resolveGetOrNull
			?: resolveMakeOrNull
			?: this

val Value.resolveFunctionApplyOrNull: Value?
	get() =
		linkOrNull
			?.run {
				tail.functionOrNull?.let { function ->
					head.fieldOrNull?.valueOrNull(applyName)?.let { given ->
						function.apply(given)
					}
				}
			}

fun Value.bodyOrNull(name: String): Value? =
	lineOrNull?.bodyOrNull(name)

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

val Value.bodyOrNull: Value?
	get() =
		linkOrNull?.onlyFieldOrNull?.value

fun Line.bodyOrNull(name: String): Value? =
	fieldOrNull?.valueOrNull(name)

fun Value.getOrNull(name: String): Value? =
	bodyOrNull?.selectOrNull(name)

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

fun Value.orNull(name: String): Value? =
	lineOrNull?.orNull(name)?.let { this }

val Line.textOrNull: String?
	get() =
		fieldOrNull?.valueOrNull("text")?.lineOrNull?.nativeOrNull?.any as? String

val Line.numberOrNull: Number?
	get() =
		fieldOrNull?.valueOrNull("number")?.lineOrNull?.nativeOrNull?.any as? Number

val Value.textOrNull: String?
	get() =
		lineOrNull?.textOrNull

val Value.numberOrNull: Number?
	get() =
		lineOrNull?.numberOrNull

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
		lineOrNull?.fieldOrNull?.onlyNameOrNull

fun Value.resolveInfixOrNull(name: String, fn: Value.(Value) -> Value?): Value? =
	linkOrNull?.run {
		tail.let { lhs ->
			head.fieldOrNull?.valueOrNull(name)?.let { rhs ->
				lhs.fn(rhs)
			}
		}
	}

fun Value.resolvePrefixOrNull(name: String, fn: (Value) -> Value?): Value? =
	resolveInfixOrNull(name) { rhs ->
		resolveEmptyOrNull {
			fn(rhs)
		}
	}

fun Value.resolvePostfixOrNull(name: String, fn: Value.() -> Value?): Value? =
	resolveInfixOrNull(name) { rhs ->
		rhs.resolveEmptyOrNull {
			fn()
		}
	}

fun Value.resolveOrNull(name: String, fn: () -> Value?): Value? =
	resolveInfixOrNull(name) { rhs ->
		resolveEmptyOrNull {
			rhs.resolveEmptyOrNull {
				fn()
			}
		}
	}

fun Value.resolveEmptyOrNull(fn: () -> Value?): Value? =
	ifOrNull(this is EmptyValue) {
		fn()
	}

val Boolean.isValue
	get() =
		value(isName lineTo value(if (this) yesName else noName))

val Value.hashValue
	get() =
		value(hashName lineTo value(line(literal(hashCode()))))

val Value.repeatValueOrNull: Value?
	get() =
		resolvePostfixOrNull(repeatName) { this }

fun Value.unlinkOrNull(fn: Value.(Value) -> Value?): Value? =
	linkOrNull?.run {
		tail.let { lhs ->
			head.fieldOrNull?.value?.let { rhs ->
				lhs.fn(rhs)
			}
		}
	}

fun Value.lineOrNull(name: String): Line? =
	when (this) {
		EmptyValue -> null
		is LinkValue -> link.lineOrNull(name)
	}

fun Link.lineOrNull(name: String): Line? =
	head.orNull(name) ?: tail.lineOrNull(name)

fun Line.orNull(name: String): Line? =
	orNullIf { selectName != name }

fun Value.resolveOrNull(lhsName: String, rhsName: String, fn: Value.(Value) -> Value?): Value? =
	linkOrNull?.run {
		head.bodyOrNull(rhsName)?.let { rhs ->
			tail.orNull(lhsName)?.let { lhs ->
				lhs.fn(rhs)
			}
		}
	}

fun Value.resolveOrNull(name: String, fn: (Value) -> Value?): Value? =
	bodyOrNull(name)?.let(fn)
