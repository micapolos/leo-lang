@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo.base.ifOrNull
import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.untyped.minusName
import leo14.untyped.plusName
import leo14.untyped.textName
import leo14.untyped.timesName

typealias Erase = () -> Value
typealias EraseOf<T> = () -> T

data class Compiled(val type: Type, val erase: Erase)
data class CompiledLink(val lhs: Compiled, val line: CompiledLine)
data class CompiledFunction(val function: TypeFunction, val erase: Erase)
data class CompiledAnything(val erase: Erase)
data class CompiledLine(val line: TypeLine, val erase: Erase)
data class CompiledField(val name: String, val rhs: Compiled)

infix fun Type.compiled(erase: Erase) = Compiled(this, erase)
infix fun Compiled.linkTo(line: CompiledLine) = CompiledLink(this, line)
infix fun TypeFunction.compiled(erase: Erase) = CompiledFunction(this, erase)
infix fun TypeLine.compiled(erase: Erase) = CompiledLine(this, erase)
infix fun String.fieldTo(rhs: Compiled) = CompiledField(this, rhs)
fun anythingCompiled(erase: Erase) = CompiledAnything(erase)

inline fun <L, R> Compiled.apply(rhs: Compiled, type: Type, crossinline fn: L.(R) -> Value): Compiled =
	erase.let { lhsErase ->
		rhs.erase.let { rhsErase ->
			type.compiled { fn(lhsErase() as L, rhsErase() as R) }
		}
	}

val emptyCompiled = emptyType.compiled { null }
val nothingCompiled = nothingType.compiled { null!! }
val Compiled.isEmpty get() = type.isEmpty
val Compiled.value get() = erase()

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

fun Compiled.matchEmpty(fn: () -> Compiled?): Compiled? =
	ifOrNull(type is EmptyType) { fn() }

fun Compiled.matchAnything(fn: (Erase) -> Compiled?): Compiled? =
	ifOrNull(type is AnythingType) { fn(erase) }

fun Compiled.matchFunction(fn: (TypeFunction, Erase) -> Compiled?): Compiled? =
	(type as? FunctionType)?.function?.let { function -> fn(function, erase) }

val Compiled.linkOrNull: CompiledLink?
	get() =
		(type as? LinkType)?.link?.let { link ->
			if (link.lhs.isStatic || link.line.isStatic)
				link.lhs.compiled(erase) linkTo link.line.compiled(erase)
			else
				link.lhs.compiled { (erase() as Pair<*, *>).first } linkTo
					link.line.compiled { (erase() as Pair<*, *>).second }
		}

val CompiledLine.fieldOrNull: CompiledField?
	get() =
		(line as? FieldTypeLine)?.field?.let { field ->
			field.name fieldTo field.rhs.compiled(erase)
		}

fun Compiled.matchInfix(name: String, fn: Compiled.(Compiled) -> Compiled?): Compiled? =
	linkOrNull?.let { link ->
		link.line.fieldOrNull?.let { field ->
			ifOrNull(field.name == name) {
				link.lhs.fn(field.rhs)
			}
		}
	}
