@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.untyped.*

typealias Erase = () -> Value
typealias EraseOf<T> = () -> T

data class Compiled<out T>(val type: Type, val erase: EraseOf<T>)
data class CompiledLink<out L, out R>(val lhs: Compiled<L>, val line: CompiledLine<R>)
data class CompiledFunction<out T>(val function: TypeFunction, val erase: EraseOf<T>)
data class CompiledAnything(val erase: Erase)
data class CompiledLine<out T>(val typeLine: TypeLine, val erase: EraseOf<T>)
data class CompiledField<out T>(val name: String, val rhs: Compiled<T>)

infix fun <T> Type.compiled(erase: EraseOf<T>) = Compiled(this, erase)
infix fun <T> Compiled<T>.linkTo(line: CompiledLine<T>) = CompiledLink(this, line)
infix fun <T> TypeFunction.compiled(erase: EraseOf<T>) = CompiledFunction(this, erase)
infix fun <T> TypeLine.compiled(erase: EraseOf<T>) = CompiledLine(this, erase)
infix fun <T> String.fieldTo(rhs: Compiled<T>) = CompiledField(this, rhs)
fun <T> anythingCompiled(erase: EraseOf<T>) = CompiledAnything(erase)

inline fun <L, R, O> Compiled<L>.apply(rhs: Compiled<R>, type: Type, crossinline fn: L.(R) -> O): Compiled<O> =
	erase.let { lhsErase ->
		rhs.erase.let { rhsErase ->
			type.compiled { fn(lhsErase(), rhsErase()) }
		}
	}

val emptyCompiled: Compiled<*> = emptyType.compiled { null }
val nothingCompiled = nothingType.compiled { null!! }
val Compiled<*>.isEmpty get() = type.isEmpty
val <T> Compiled<T>.value: T get() = erase()

val <T> Compiled<T>.typed: Typed get() = type typed value

fun Compiled<*>.apply(literal: Literal): Compiled<*> =
	if (isEmpty) emptyType.plus(literal.typeLine).compiled { literal.value }
	else type.plus(literal.typeLine).compiled { value to literal.value }

fun Compiled<*>.apply(begin: Begin, rhs: Compiled<*>): Compiled<*> =
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

fun Compiled<*>.append(begin: Begin, rhs: Compiled<*>): Compiled<*> =
	type.plus(begin.string lineTo rhs.type).compiled { value to rhs.value }

fun Compiled<*>.matchEmpty(fn: () -> Compiled<*>?): Compiled<*>? =
	ifOrNull(type is EmptyType) { fn() }

fun Compiled<*>.matchAnything(fn: (Erase) -> Compiled<*>?): Compiled<*>? =
	ifOrNull(type is AnythingType) { fn(erase) }

fun Compiled<*>.matchFunction(fn: (TypeFunction, Erase) -> Compiled<*>?): Compiled<*>? =
	(type as? FunctionType)?.function?.let { function -> fn(function, erase) }

fun <L, R, O> Compiled<*>.linkApply(targetType: Type, fn: L.(R) -> O): Compiled<O>? =
	type.linkOrNull?.let { link ->
		if (link.lhs.isStatic || link.line.isStatic)
			targetType.compiled {
				erase().let {
					(it as L).fn(it as R)
				}
			}
		else
			targetType.compiled {
				erase().let {
					(it as Pair<*, *>)
					(it.first as L).fn(it.second as R)
				}
			}
	}

val Compiled<*>.linkOrNull: CompiledLink<*, *>?
	get() =
		(type as? LinkType)?.link?.let { link ->
			if (link.lhs.isStatic || link.line.isStatic)
				link.lhs.compiled(erase) linkTo link.line.compiled(erase)
			else
				link.lhs.compiled { (erase() as Pair<*, *>).first } linkTo
					link.line.compiled { (erase() as Pair<*, *>).second }
		}

val CompiledLine<*>.fieldOrNull: CompiledField<*>?
	get() =
		(typeLine as? FieldTypeLine)?.field?.let { field ->
			field.name fieldTo field.rhs.compiled(erase)
		}

fun Compiled<*>.matchInfix(name: String, fn: Compiled<*>.(Compiled<*>) -> Compiled<*>?): Compiled<*>? =
	linkOrNull?.let { link ->
		link.line.fieldOrNull?.let { field ->
			ifOrNull(field.name == name) {
				link.lhs.fn(field.rhs)
			}
		}
	}

fun Compiled<*>.get(name: String): Compiled<*>? =
	linkOrNull?.let { link ->
		link.lhs.matchEmpty {
			link.line.fieldOrNull?.rhs?.select(name)
		}
	}

fun Compiled<*>.select(name: String): Compiled<*>? =
	linkOrNull?.select(name)

fun CompiledLink<*, *>.select(name: String): Compiled<*>? =
	line.select(name) ?: lhs.select(name)

fun CompiledLine<*>.select(name: String): Compiled<*>? =
	when (name) {
		textName ->
			notNullIf(typeLine == textTypeLine) {
				textType.compiled(erase)
			}
		numberName ->
			notNullIf(typeLine == numberTypeLine) {
				numberType.compiled(erase)
			}
		nativeName ->
			notNullIf(typeLine == nativeTypeLine) {
				nativeType.compiled(erase)
			}
		functionName -> TODO()
		else -> fieldOrNull?.select(name)
	}

fun CompiledField<*>.select(name: String): Compiled<*>? =
	notNullIf(this.name == name) {
		emptyType.plus(name lineTo rhs.type).compiled(rhs.erase)
	}

