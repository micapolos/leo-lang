package leo20

import leo.base.SeqNode
import leo.base.fold
import leo.base.ifOrNull
import leo.base.mapFirstOrNull
import leo.base.nodeOrNull
import leo.base.seqNode
import leo13.Stack
import leo13.first
import leo13.linkOrNull
import leo13.onlyOrNull
import leo13.push
import leo13.seq
import leo13.seqNode
import leo13.stack
import leo14.Script
import leo14.bigDecimal
import leo14.nameStackOrNull
import java.math.BigDecimal

data class Value(val lineStack: Stack<Line>)

sealed class Line
data class FieldLine(val field: Field) : Line()
data class StringLine(val string: String) : Line()
data class NumberLine(val number: Number) : Line()
data class FunctionLine(val function: Function) : Line()

data class Field(val name: String, val rhs: Value)

val emptyValue = Value(stack())
fun Value.plus(line: Line) = Value(lineStack.push(line))
fun value(vararg lines: Line) = emptyValue.fold(lines) { plus(it) }
infix fun String.lineTo(rhs: Value): Line = FieldLine(Field(this, rhs))
fun line(function: Function): Line = FunctionLine(function)
fun line(string: String): Line = StringLine(string)
fun line(int: Int): Line = NumberLine(int.toDouble().bigDecimal)
fun line(double: Double): Line = NumberLine(double.bigDecimal)
fun line(bigDecimal: BigDecimal): Line = NumberLine(bigDecimal)

val Line.selectName: String
	get() =
		when (this) {
			is FieldLine -> field.name
			is StringLine -> "text"
			is NumberLine -> "number"
			is FunctionLine -> "function"
		}

val Line.fieldOrNull get() = (this as? FieldLine)?.field
val Line.functionOrNull get() = (this as? FunctionLine)?.function

val Value.bodyOrNull: Value?
	get() =
		lineStack.onlyOrNull?.fieldOrNull?.rhs

fun Value.lineOrNull(name: String): Line? =
	lineStack.first { it.selectName == name }

fun Value.getOrNull(name: String): Value? =
	bodyOrNull?.lineOrNull(name)?.let { value(it) }

fun Value.make(name: String): Value =
	value(name lineTo this)

fun Value.applyOrNull(param: Value): Value? =
	lineStack.onlyOrNull?.functionOrNull?.apply(param)

fun Value.apply(param: Value): Value =
	applyOrNull(param) ?: plus("apply" lineTo param)

fun Value.unsafeGet(name: String) = getOrNull(name)!!
val Value.unsafeNumber get() = (lineStack.onlyOrNull as NumberLine).number
val Value.unsafeString get() = (lineStack.onlyOrNull as StringLine).string
fun Value.unsafeNumberPlus(value: Value) = value(line(unsafeNumber.toDouble().plus(value.unsafeNumber.toDouble())))
fun Value.unsafeNumberMinus(value: Value) = value(line(unsafeNumber.toDouble().minus(value.unsafeNumber.toDouble())))

val Boolean.name get() = if (this) "true" else "false"
val Boolean.value get() = value("boolean" lineTo value(name lineTo value()))

val Value.resolveGetOrNull: Value?
	get() =
		lineStack.linkOrNull?.let { lineLink ->
			lineLink.value.fieldOrNull?.let { field ->
				ifOrNull(field.rhs == value()) {
					Value(lineLink.stack).getOrNull(field.name)
				}
			}
		}

val Value.resolve: Value
	get() =
		resolveGetOrNull ?: this

fun Value.lineOrNull(name: String, vararg names: String): Line? =
	lineOrNull(seqNode(name, *names))

fun Value.lineOrNull(nameSeqNode: SeqNode<String>): Line? =
	lineStack.seq.mapFirstOrNull { orNull(nameSeqNode) }

fun Line.orNull(nameSeqNode: SeqNode<String>): Line? =
	ifOrNull(nameSeqNode.first == selectName) {
		nameSeqNode.remaining.nodeOrNull.let { remainingNodeOrNull ->
			if (remainingNodeOrNull == null) this
			else fieldOrNull?.rhs?.lineOrNull(remainingNodeOrNull)
		}
	}

fun Value.getOrNull(name: String, vararg names: String): Value? =
	getOrNull(seqNode(name, *names))

fun Value.getOrNull(nameSeqNode: SeqNode<String>): Value? =
	bodyOrNull?.lineOrNull(nameSeqNode)?.let { value(it) }

fun Value.getOrNull(script: Script): Value? =
	script.nameStackOrNull?.linkOrNull?.seqNode?.let { getOrNull(it) }