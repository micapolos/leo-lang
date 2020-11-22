package leo21.evaluated

import leo.base.fold
import leo.base.notNullIf
import leo13.Link
import leo13.linkTo
import leo14.Number
import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.value.Value
import leo14.lambda.value.native
import leo14.lambda.value.pair
import leo14.lambda.value.plus
import leo14.lambda.value.term
import leo14.lambda.value.value
import leo14.lineTo
import leo14.script
import leo21.compiled.Compiled
import leo21.prim.Prim
import leo21.prim.nilPrim
import leo21.prim.number
import leo21.prim.string
import leo21.type.Type
import leo21.type.isEmpty
import leo21.type.isStatic
import leo21.type.linkOrNull
import leo21.type.numberType
import leo21.type.plus
import leo21.type.stringType
import leo21.type.type

data class Evaluated(val value: Value<Prim>, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "evaluated" lineTo script(value.reflectScriptLine, type.reflectScriptLine)
}

infix fun Value<Prim>.of(type: Type) = Evaluated(this, type)

val emptyEvaluated = Evaluated(value(nilPrim), type())

fun evaluated(vararg lines: LineEvaluated): Evaluated =
	emptyEvaluated.fold(lines) { plus(it) }

val Evaluated.compiled: Compiled
	get() =
		Compiled(value.term, type)

fun evaluated(lineEvaluated: LineEvaluated): Evaluated =
	lineEvaluated.value of type(lineEvaluated.line)

fun evaluated(string: String) = evaluated(string.lineEvaluated)
fun evaluated(number: Number) = evaluated(number.lineEvaluated)
fun evaluated(double: Double) = evaluated(double.lineEvaluated)

val Evaluated.rhsOrNull: Evaluated?
	get() =
		onlyLineOrNull?.fieldOrNull?.rhs

val Evaluated.numberOrNull: Number? get() = notNullIf(type == numberType) { value.native.number }
val Evaluated.stringOrNull: String? get() = notNullIf(type == stringType) { value.native.string }

val Evaluated.number: Number get() = numberOrNull!!
val Evaluated.string: String get() = stringOrNull!!

fun Evaluated.apply(evaluated: Evaluated): Evaluated =
	onlyLineOrNull!!
		.arrowOrNull!!
		.apply(evaluated)

fun Evaluated.plus(rhs: LineEvaluated): Evaluated =
	(if (type.isStatic)
		if (rhs.line.isStatic) nilValue
		else rhs.value
	else
		if (rhs.line.isStatic) value
		else value.plus(rhs.value)) of type.plus(rhs.line)

val Evaluated.linkOrNull: Link<Evaluated, LineEvaluated>?
	get() =
		type.linkOrNull?.let { structLink ->
			(if (structLink.tail.isStatic)
				if (structLink.head.isStatic) nilValue to nilValue
				else nilValue to value
			else
				if (structLink.head.isStatic) value to nilValue
				else value.pair { lhs, rhs -> lhs to rhs }).let { (lhs, rhs) ->
				lhs.of(structLink.tail) linkTo rhs.of(structLink.head)
			}
		}

val Evaluated.onlyLineOrNull: LineEvaluated?
	get() =
		linkOrNull?.let { link ->
			notNullIf(link.tail.type.isEmpty) {
				link.head
			}
		}

val Link<Evaluated, LineEvaluated>.evaluated: Evaluated get() = tail
val Link<Evaluated, LineEvaluated>.lineEvaluated: LineEvaluated get() = head
