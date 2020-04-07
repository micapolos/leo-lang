package leo14.untyped.typed

import leo.base.reverseStack
import leo13.fold
import leo14.*
import leo14.Number
import leo14.untyped.minusName
import leo14.untyped.plusName
import leo14.untyped.textName
import leo14.untyped.timesName

data class Compiled(val type: Type, val valueFn: ValueFn)

val emptyCompiled = Compiled(emptyType) { null }
val Compiled.isEmpty get() = type.isEmpty
infix fun Type.compiled(valueFn: ValueFn) = Compiled(this, valueFn)
val Compiled.value get() = valueFn()

fun Scope.apply(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
	if (rhs.isEmpty) applyNormalized(rhs, begin, lhs)
	else applyNormalized(lhs, begin, rhs)

fun Scope.applyNormalized(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
	applyRules(lhs, begin, rhs) ?: lhs.apply(begin, rhs)

fun Scope.applyRules(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled? =
	null

fun Scope.compiled(script: Script): Compiled =
	emptyCompiled.fold(script.lineSeq.reverseStack) { line ->
		compiled(this, line)
	}

fun Scope.compiled(lhs: Compiled, line: ScriptLine): Compiled =
	when (line) {
		is LiteralScriptLine -> lhs.apply(line.literal)
		is FieldScriptLine -> apply(lhs, begin(line.field.string), compiled(line.field.rhs))
	}

fun Compiled.apply(literal: Literal): Compiled =
	if (isEmpty) emptyType.plus(literal.typeLine).compiled { literal.value }
	else type.plus(literal.typeLine).compiled { value to literal.value }

fun Compiled.apply(begin: Begin, rhs: Compiled): Compiled =
	when (type) {
		emptyType ->
			when (begin.string) {
				minusName ->
					when (rhs.type) {
						numberType2 -> numberType2.compiled { (value as Number).unaryMinus() }
						else -> null
					}
				textName ->
					when (rhs.type) {
						numberType2 -> numberType2.compiled { (value as Number).toString() }
						else -> null
					}
				else -> null
			}
		textType2 ->
			when (begin.string) {
				plusName ->
					when (rhs.type) {
						textType2 -> textType2.compiled { (value as String).plus(rhs.value as String) }
						else -> null
					}
				else -> null
			}
		numberType2 ->
			when (begin.string) {
				plusName ->
					when (rhs.type) {
						numberType2 -> textType2.compiled { (value as Number).plus(rhs.value as Number) }
						else -> null
					}
				minusName ->
					when (rhs.type) {
						numberType2 -> textType2.compiled { (value as Number).minus(rhs.value as Number) }
						else -> null
					}
				timesName ->
					when (rhs.type) {
						numberType2 -> textType2.compiled { (value as Number).times(rhs.value as Number) }
						else -> null
					}
				else -> null
			}
		is FunctionType -> TODO()
		else -> null
	} ?: append(begin, rhs)

fun Compiled.append(begin: Begin, rhs: Compiled): Compiled =
	type.plus(begin.string lineTo rhs.type).compiled { value to rhs.value }

