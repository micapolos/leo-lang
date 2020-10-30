package leo20

import leo.base.SeqNode
import leo.base.fold
import leo.base.ifOrNull
import leo.base.mapFirstOrNull
import leo.base.nodeOrNull
import leo.base.notNullIf
import leo.base.seqNode
import leo13.Stack
import leo13.first
import leo13.linkOrNull
import leo13.onlyOrNull
import leo13.push
import leo13.reverse
import leo13.seq
import leo13.seqNode
import leo13.stack
import leo14.Literal
import leo14.Script
import leo14.bigDecimal
import leo14.nameStackOrNull
import java.math.BigDecimal

data class Value(val lineStack: Stack<Line>)

sealed class Line
data class FieldLine(val field: Field) : Line()
data class NativeLine(val native: Any) : Line()
data class FunctionLine(val function: Function) : Line()

data class Field(val name: String, val rhs: Value)

val emptyValue = Value(stack())
fun Value.plus(line: Line) = Value(lineStack.push(line))
fun Value.plus(value: Value): Value = fold(value.lineStack.reverse.seq) { plus(it) }
fun value(vararg lines: Line) = emptyValue.fold(lines) { plus(it) }
infix fun String.lineTo(rhs: Value): Line = FieldLine(Field(this, rhs))
fun line(function: Function): Line = FunctionLine(function)
fun line(string: String): Line = "text" lineTo value(NativeLine(string))
fun line(bigDecimal: BigDecimal): Line = "number" lineTo value(NativeLine(bigDecimal))
fun line(int: Int): Line = line(int.bigDecimal)
fun line(double: Double): Line = line(double.bigDecimal)
fun line(literal: Literal) = literal.valueLine

val Line.selectName: String
	get() =
		when (this) {
			is FieldLine -> field.name
			is NativeLine -> "_" // Refactor to selectNameOrNull
			is FunctionLine -> "function"
		}

val Line.fieldOrNull get() = (this as? FieldLine)?.field
val Line.functionOrNull get() = (this as? FunctionLine)?.function

val Value.bodyOrNull: Value?
	get() =
		lineStack.onlyOrNull?.fieldOrNull?.rhs

fun Value.bodyOrNull(name: String) =
	lineStack.onlyOrNull?.fieldOrNull?.let { field ->
		notNullIf(field.name == name) {
			field.rhs
		}
	}

fun Value.lineOrNull(name: String): Line? =
	lineStack.first { it.selectName == name }

fun Value.getOrNull(name: String): Value? =
	bodyOrNull?.lineOrNull(name)?.let { value(it) }

fun Value.make(name: String): Value =
	value(name lineTo this)

fun Value.makeOrNull(script: Script): Value? =
	script.nameStackOrNull?.reverse?.let { nameStack ->
		fold(nameStack.seq) { make(it) }
	}

fun Value.applyOrNull(param: Value): Value? =
	lineStack.onlyOrNull?.functionOrNull?.apply(param)

fun Value.apply(param: Value): Value =
	applyOrNull(param) ?: plus("apply" lineTo param)

val Line.nativeOrNull get() = (this as? NativeLine)?.native
val Value.nativeOrNull get() = lineStack.onlyOrNull?.nativeOrNull

val Value.bigDecimalOrNull get() = lineStack.onlyOrNull?.bigDecimalOrNull
val Value.stringOrNull get() = lineStack.onlyOrNull?.stringOrNull

val Line.bigDecimalOrNull get() = fieldOrNull?.bigDecimalOrNull
val Line.stringOrNull get() = fieldOrNull?.stringOrNull

val Field.bigDecimalOrNull
	get() =
		ifOrNull(name == "number") {
			rhs.nativeOrNull as? BigDecimal
		}

val Field.stringOrNull
	get() =
		ifOrNull(name == "text") {
			rhs.nativeOrNull as? String
		}

fun Value.unsafeGet(name: String) = getOrNull(name)!!
val Value.unsafeBigDecimal get() = bodyOrNull("number")!!.nativeOrNull as BigDecimal
val Value.unsafeString get() = bodyOrNull("text")!!.nativeOrNull as String
fun Value.unsafeNumberPlus(value: Value) = value(line(unsafeBigDecimal.plus(value.unsafeBigDecimal)))
fun Value.unsafeNumberMinus(value: Value) = value(line(unsafeBigDecimal.minus(value.unsafeBigDecimal)))
fun Value.unsafeTextAppend(value: Value) = value(line(unsafeString.plus(value.unsafeString)))

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