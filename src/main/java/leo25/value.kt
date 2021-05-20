package leo25

import leo.base.fold
import leo.base.notNullIf
import leo14.*

sealed class Value {
	override fun toString() = script.toString()
}

object EmptyValue : Value()
data class LinkValue(val link: Link) : Value() {
	override fun toString() = super.toString()
}

data class Native(val any: Any?)

sealed class Line
data class LiteralLine(val literal: Literal) : Line()
data class FunctionLine(val function: Function) : Line()
data class FieldLine(val field: Field) : Line()
data class NativeLine(val native: Native) : Line()

data class Link(val tail: Value, val head: Line)
data class Field(val name: String, val value: Value)

infix fun String.fieldTo(value: Value): Field = Field(this, value)
infix fun String.lineTo(value: Value): Line = FieldLine(this fieldTo value)
fun line(field: Field): Line = FieldLine(field)
fun line(literal: Literal): Line = LiteralLine(literal)
fun line(string: String): Line = line(literal(string))
fun line(function: Function): Line = FunctionLine(function)
fun line(native: Native): Line = NativeLine(native)

fun native(any: Any?) = Native(any)

operator fun Value.plus(line: Line): Value = LinkValue(Link(this, line))
val emptyValue: Value get() = EmptyValue
fun value(vararg lines: Line) = emptyValue.fold(lines) { plus(it) }
fun value(name: String) = value(name lineTo value())

val Line.functionOrNull: Function? get() = (this as? FunctionLine)?.function
val Line.literalOrNull: Literal? get() = (this as? LiteralLine)?.literal
val Line.stringOrNull: String? get() = literalOrNull?.stringOrNull
val Line.fieldOrNull: Field? get() = (this as? FieldLine)?.field

val Value.linkOrNull: Link? get() = (this as? LinkValue)?.link
val Value.functionOrNull: Function? get() = linkOrNull?.onlyLineOrNull?.functionOrNull
val Value.stringOrNull: String? get() = linkOrNull?.onlyLineOrNull?.stringOrNull

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
			?: resolveTextPlusTextOrNull
			?: resolveNameOrNull
			?: this

val Value.resolveNameOrNull: Value?
	get() =
		linkOrNull?.let { link ->
			link.head.fieldOrNull?.onlyNameOrNull?.let { name ->
				link.tail.resolve(name)
			}
		}

val Value.resolveFunctionApplyOrNull: Value?
	get() =
		linkOrNull
			?.run {
				tail.resolveFunctionOrNull?.let { function ->
					head.fieldOrNull?.valueOrNull("apply")?.let { given ->
						function.apply(given)
					}
				}
			}

val Value.resolveTextPlusTextOrNull: Value?
	get() =
		linkOrNull
			?.run {
				tail.resolveTextOrNull?.let { lhs ->
					head.fieldOrNull?.valueOrNull("plus")?.resolveTextOrNull?.let { rhs ->
						value(line(literal(lhs.plus(rhs))))
					}
				}
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
			is FunctionLine -> functionName
			is LiteralLine -> literal.selectName
			is NativeLine -> nativeName
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

val Value.resolveFunctionOrNull: Function?
	get() =
		functionOrNull

val Value.resolveTextOrNull: String?
	get() =
		stringOrNull

val Field.onlyNameOrNull: String?
	get() =
		notNullIf(value is EmptyValue) { name }