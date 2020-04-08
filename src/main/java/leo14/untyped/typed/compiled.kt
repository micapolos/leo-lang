package leo14.untyped.typed

import leo.base.ifOrNull
import leo14.*
import leo14.Number
import leo14.lambda.runtime.Fn
import leo14.untyped.minusName
import leo14.untyped.plusName
import leo14.untyped.textName
import leo14.untyped.timesName

data class Compiled(val type: Type, val valueFn: Fn)

infix fun Type.compiled(valueFn: Fn) = Compiled(this, valueFn)
val emptyCompiled = emptyType.compiled { null }
val nothingCompiled = nothingType.compiled { null!! }
val Compiled.isEmpty get() = type.isEmpty
val Compiled.value get() = valueFn(null)

val Compiled.typed: Typed get() = type.typed(value)

fun Compiled.apply(literal: Literal): Compiled =
	if (isEmpty) emptyType.plus(literal.typeLine).compiled { literal.value }
	else type.plus(literal.typeLine).compiled { value to literal.value }

fun Compiled.apply(begin: Begin, rhs: Compiled): Compiled =
	when (type) {
		emptyType ->
			when (begin.string) {
				minusName ->
					when (rhs.type) {
						numberType -> numberType.compiled { (value as Number).unaryMinus() }
						else -> null
					}
				textName ->
					when (rhs.type) {
						numberType -> numberType.compiled { (value as Number).toString() }
						else -> null
					}
				else -> null
			}
		textType ->
			when (begin.string) {
				plusName ->
					when (rhs.type) {
						textType -> textType.compiled { (value as String).plus(rhs.value as String) }
						else -> null
					}
				else -> null
			}
		numberType ->
			when (begin.string) {
				plusName ->
					when (rhs.type) {
						numberType -> textType.compiled { (value as Number).plus(rhs.value as Number) }
						else -> null
					}
				minusName ->
					when (rhs.type) {
						numberType -> textType.compiled { (value as Number).minus(rhs.value as Number) }
						else -> null
					}
				timesName ->
					when (rhs.type) {
						numberType -> textType.compiled { (value as Number).times(rhs.value as Number) }
						else -> null
					}
				else -> null
			}
		is FunctionType -> TODO()
		else -> null
	} ?: append(begin, rhs)

fun Compiled.append(begin: Begin, rhs: Compiled): Compiled =
	type.plus(begin.string lineTo rhs.type).compiled { value to rhs.value }

fun Compiled.matchInfix(name: String, fn: (Compiled, Compiled) -> Compiled?): Compiled? =
	(type as? LinkType)?.link?.let { link ->
		(link.line as? FieldTypeLine)?.field?.let { field ->
			ifOrNull(field.name == name) {
				if (link.lhs.isStatic || field.rhs.isStatic)
					fn(
						link.lhs.compiled(valueFn),
						field.rhs.compiled(valueFn))
				else
					fn(
						link.lhs.compiled { (value as Pair<*, *>).first },
						field.rhs.compiled { (value as Pair<*, *>).second })
			}
		}
	}