@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.untyped.*

typealias Erase = () -> Value

data class Compiled<out T>(val type: Type, val expression: Expression<T>)
data class CompiledLink<out L, out R>(val lhs: Compiled<L>, val line: CompiledLine<R>)
data class CompiledFunction<out T>(val function: TypeFunction, val expression: Expression<T>)
data class CompiledAnything(val expression: Expression<*>)
data class CompiledLine<out T>(val typeLine: TypeLine, val expression: Expression<T>)
data class CompiledField<out T>(val name: String, val rhs: Compiled<T>)

infix fun <T> Type.compiled(expression: Expression<T>) = Compiled(this, expression)
infix fun <T> Type.compiled(value: T) = Compiled(this, constant(value).expression)
infix fun <T> Type.compiled(evaluate: () -> T) = Compiled(this, dynamic(evaluate).expression)
infix fun <L, R> Compiled<L>.linkTo(line: CompiledLine<R>) = CompiledLink(this, line)
infix fun <T> TypeFunction.compiled(expression: Expression<T>) = CompiledFunction(this, expression)
infix fun <T> TypeLine.compiled(expression: Expression<T>) = CompiledLine(this, expression)
infix fun <T> String.fieldTo(rhs: Compiled<T>) = CompiledField(this, rhs)
fun <T> anythingCompiled(expression: Expression<T>) = CompiledAnything(expression)

inline fun <L, R, O> Compiled<L>.apply(rhs: Compiled<R>, type: Type, crossinline fn: L.(R) -> O): Compiled<O> =
	type.compiled(expression.doApply(rhs.expression, fn))

val emptyCompiled: Compiled<*> = emptyType.compiled { null }
val nothingCompiled = nothingType.compiled { null!! }
val Compiled<*>.isEmpty get() = type.isEmpty
val <T> Compiled<T>.value: T get() = expression.value

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

fun Compiled<*>.matchFunction(fn: (TypeFunction, Expression<*>) -> Compiled<*>?): Compiled<*>? =
	(type as? FunctionType)?.function?.let { function -> fn(function, expression) }

fun <L, R, O> Compiled<*>.linkApply(targetType: Type, fn: L.(R) -> O): Compiled<O>? =
	type.linkOrNull?.let { link ->
		if (link.lhs.isStatic || link.line.isStatic)
			targetType.compiled(expression.doApply { (this as L).fn(this as R) })
		else
			targetType.compiled(
				expression.doApply {
					(this as Pair<*, *>)
					(first as L).fn(second as R)
				})
	}

val Compiled<*>.linkOrNull: CompiledLink<*, *>?
	get() =
		(type as? LinkType)?.link?.let { link ->
			if (link.lhs.isStatic || link.line.isStatic)
				link.lhs.compiled(expression) linkTo link.line.compiled(expression)
			else
				link.lhs.compiled(expression.doApply { (this as Pair<*, *>).first }) linkTo
					link.line.compiled(expression.doApply { (this as Pair<*, *>).second })
		}

val CompiledLine<*>.fieldOrNull: CompiledField<*>?
	get() =
		(typeLine as? FieldTypeLine)?.field?.let { field ->
			field.name fieldTo field.rhs.compiled(expression)
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
				textType.compiled(expression)
			}
		numberName ->
			notNullIf(typeLine == numberTypeLine) {
				numberType.compiled(expression)
			}
		nativeName ->
			notNullIf(typeLine == nativeTypeLine) {
				nativeType.compiled(expression)
			}
		functionName -> TODO()
		else -> fieldOrNull?.select(name)
	}

fun CompiledField<*>.select(name: String): Compiled<*>? =
	notNullIf(this.name == name) {
		emptyType.plus(name lineTo rhs.type).compiled(rhs.expression)
	}

