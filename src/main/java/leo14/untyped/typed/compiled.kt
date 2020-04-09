@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.*
import leo14.lambda.runtime.Value
import leo14.untyped.*

data class Compiled(val type: Type, val expression: Expression)
data class CompiledLink(val lhs: Compiled, val line: CompiledLine)
data class CompiledFunction(val function: TypeFunction, val expression: Expression)
data class CompiledAnything(val expression: Expression)
data class CompiledLine(val typeLine: TypeLine, val expression: Expression)
data class CompiledField(val name: String, val rhs: Compiled)

infix fun Type.compiled(expression: Expression) = Compiled(this, expression)
infix fun Type.compiled(value: Value) = Compiled(this, expression(value))
infix fun Type.compiled(evaluate: Evaluate) = Compiled(this, expression(evaluate))
infix fun Compiled.linkTo(line: CompiledLine) = CompiledLink(this, line)
infix fun TypeFunction.compiled(expression: Expression) = CompiledFunction(this, expression)
infix fun TypeLine.compiled(expression: Expression) = CompiledLine(this, expression)
infix fun String.fieldTo(rhs: Compiled) = CompiledField(this, rhs)
fun anythingCompiled(expression: Expression) = CompiledAnything(expression)

inline fun Compiled.apply(rhs: Compiled, type: Type, crossinline fn: Value.(Value) -> Value): Compiled =
	type.compiled(expression.doApply(rhs.expression, fn))

val emptyCompiled: Compiled = emptyType.compiled { null }
val nothingCompiled = nothingType.compiled { null!! }
val Compiled.isEmpty get() = type.isEmpty
val Compiled.value: Value get() = expression.value

val Compiled.evaluate: Compiled get() = type.compiled(expression.evaluate)
val Compiled.typed: Typed get() = type typed value

fun Compiled.apply(literal: Literal): Compiled =
	if (isEmpty) emptyType.plus(literal.typeLine).compiled { literal.value }
	else type.plus(literal.typeLine).compiled { value to literal.value }

fun Compiled.apply(begin: Begin, rhs: Compiled): Compiled =
	null
		?: applyPrimitives(begin, rhs)
		?: applyFunctionApply(rhs)
		?: append(begin, rhs)

val Compiled.apply: Compiled
	get() =
		null
			?: applyMinusNumber
			?: applyNumberPlusNumber
			?: applyNumberMinusNumber
			?: applyNumberTimesNumber
			?: applyClassJavaText
			?: applyArrayJavaList
			?: this

val Compiled.applyMinusNumber: Compiled?
	get() =
		type.matchPrefix(minusName) {
			matchNumber {
				numberType.compiled(expression.doApply { -asNumber })
			}
		}

val Compiled.applyNumberPlusNumber: Compiled?
	get() =
		type.matchInfix(plusName) { rhs ->
			matchNumber {
				rhs.matchNumber {
					numberType.compiled(expression.doApply {
						this as Pair<*, *>
						first.asNumber + second.asNumber
					})
				}
			}
		}

val Compiled.applyNumberMinusNumber: Compiled?
	get() =
		type.matchInfix(minusName) { rhs ->
			matchNumber {
				rhs.matchNumber {
					numberType.compiled(expression.doApply {
						this as Pair<*, *>
						first.asNumber - second.asNumber
					})
				}
			}
		}

val Compiled.applyNumberTimesNumber: Compiled?
	get() =
		type.matchInfix(timesName) { rhs ->
			matchNumber {
				rhs.matchNumber {
					numberType.compiled(expression.doApply {
						this as Pair<*, *>
						first.asNumber * second.asNumber
					})
				}
			}
		}

val Compiled.applyClassJavaText: Compiled?
	get() =
		type.matchPrefix(className) {
			matchPrefix(javaName) {
				matchText {
					type(className lineTo nativeType)
						.compiled(expression.doApply {
							asString.loadClass
						})
				}
			}
		}

val Compiled.applyArrayJavaList: Compiled?
	get() =
		type.matchPrefix(arrayName) {
			matchPrefix(javaName) {
				matchPrefix(listName) {
					matchRepeating {
						matchLine {
							type(arrayName lineTo nativeType).compiled("This should be an array")
						}
					}
				}
			}
		}

fun Compiled.applyPrimitives(begin: Begin, rhs: Compiled): Compiled? =
	when (type.plus(begin.string(rhs.type))) {
		minusNumberType ->
			numberType.compiled(expression.numberUnaryMinus)
		textNumberType ->
			numberType.compiled(expression.numberString)
		textPlusTextType ->
			textType.compiled(expression.stringPlusString(rhs.expression))
		numberPlusNumberType ->
			numberType.compiled(expression.numberPlusNumber(rhs.expression))
		numberMinusNumberType ->
			numberType.compiled(expression.numberMinusNumber(rhs.expression))
		numberTimesNumberType ->
			numberType.compiled(expression.numberTimesNumber(rhs.expression))
		else -> null
	}

fun Compiled.applyFunctionApply(rhs: Compiled): Compiled? =
	type.functionOrNull?.let { function ->
		notNullIf(function.from == rhs.type) {
			function.to.compiled(expression.invoke(rhs.expression))
		}
	}

fun Compiled.append(literal: Literal): Compiled =
	type.plus(literal.typeLine).let { newType ->
		if (type.isEmpty) newType.compiled(literal.value)
		else newType.compiled(expression.doApply(literal.expression) { this to it })
	}

fun Compiled.append(begin: Begin, rhs: Compiled): Compiled =
	type.plus(begin.string lineTo rhs.type).let { newType ->
		if (type.isEmpty) newType.compiled(rhs.expression)
		else newType.compiled(expression.doApply(rhs.expression) { this to it })
	}

fun Compiled.matchEmpty(fn: () -> Compiled?): Compiled? =
	ifOrNull(type is EmptyType) { fn() }

fun Compiled.matchFunction(fn: (TypeFunction, Expression) -> Compiled?): Compiled? =
	(type as? FunctionType)?.function?.let { function -> fn(function, expression) }

fun Compiled.linkApply(targetType: Type, fn: Value.(Value) -> Value): Compiled? =
	type.linkOrNull?.let { link ->
		if (link.lhs.isStatic || link.line.isStatic)
			targetType.compiled(expression.doApply { fn(this) })
		else
			targetType.compiled(
				expression.doApply {
					(this as Pair<*, *>)
					first.fn(second)
				})
	}

val Compiled.linkOrNull: CompiledLink?
	get() =
		(type as? LinkType)?.link?.let { link ->
			if (link.lhs.isStatic || link.line.isStatic)
				link.lhs.compiled(expression) linkTo link.line.compiled(expression)
			else
				link.lhs.compiled(expression.doApply { (this as Pair<*, *>).first }) linkTo
					link.line.compiled(expression.doApply { (this as Pair<*, *>).second })
		}

val CompiledLine.fieldOrNull: CompiledField?
	get() =
		(typeLine as? FieldTypeLine)?.field?.let { field ->
			field.name fieldTo field.rhs.compiled(expression)
		}

fun Compiled.matchInfix(name: String, fn: Compiled.(Compiled) -> Compiled?): Compiled? =
	linkOrNull?.let { link ->
		link.line.fieldOrNull?.let { field ->
			ifOrNull(field.name == name) {
				link.lhs.fn(field.rhs)
			}
		}
	}

fun Compiled.matchPrefix(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	matchInfix(name) { rhs ->
		matchEmpty {
			fn(rhs)
		}
	}

fun Compiled.matchText(fn: Compiled.() -> Compiled?): Compiled? =
	ifOrNull(type == textType) { fn() }

fun Compiled.matchNative(fn: Compiled.() -> Compiled?): Compiled? =
	ifOrNull(type == nativeType) {
		fn()
	}

fun Compiled.get(name: String): Compiled? =
	linkOrNull?.let { link ->
		link.lhs.matchEmpty {
			link.line.fieldOrNull?.rhs?.select(name)
		}
	}

fun Compiled.select(name: String): Compiled? =
	linkOrNull?.select(name)

fun CompiledLink.select(name: String): Compiled? =
	line.select(name) ?: lhs.select(name)

fun CompiledLine.select(name: String): Compiled? =
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

fun CompiledField.select(name: String): Compiled? =
	notNullIf(this.name == name) {
		emptyType.plus(name lineTo rhs.type).compiled(rhs.expression)
	}
