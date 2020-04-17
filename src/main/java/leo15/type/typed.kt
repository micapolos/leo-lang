package leo15.type

import leo.base.applyOrNull
import leo.base.fold
import leo.base.notNullIf
import leo13.Stack
import leo14.*
import leo15.*
import leo15.lambda.append
import leo15.lambda.fn
import leo15.lambda.valueTerm

data class Typed(val expression: Expression, val type: Type) {
	override fun toString() = reflectScriptLine.string
}

data class TypedChoice(val expression: Expression, val choice: Choice)
data class TypedLine(val expression: Expression, val typeLine: TypeLine)
data class TypedField(val expression: Expression, val field: TypeField)
data class TypedLink(val lhs: Typed, val choice: TypedChoice)
data class TypedArrow(val expression: Expression, val arrow: Arrow)
data class TypedRepeating(val expression: Expression, val repeating: Repeating)

data class Match(val typeLineStack: Stack<TypeLine>, val expression: Expression, val typeOrNull: Type?)
data class Case(val name: String, val typed: Typed)

val Typed.reflectScriptLine: ScriptLine
	get() =
		typedName lineTo script(expression.reflectScriptLine).plus(ofName lineTo type.script)

infix fun Expression.of(type: Type) = Typed(this, type)
infix fun Expression.of(choice: Choice) = TypedChoice(this, choice)
infix fun Expression.of(line: TypeLine) = TypedLine(this, line)
infix fun Expression.of(field: TypeField) = TypedField(this, field)
infix fun Expression.of(arrow: Arrow) = TypedArrow(this, arrow)
infix fun Expression.of(repeating: Repeating) = TypedRepeating(this, repeating)
infix fun Typed.linkTo(choice: TypedChoice) = TypedLink(this, choice)

val emptyTyped: Typed = emptyExpression of emptyType

val Any?.javaTypedLine: TypedLine get() = valueTerm.constantExpression.of(javaTypeLine)
val Any?.javaTyped: Typed get() = typed(javaTypedLine)

val Int.typedLine: TypedLine get() = numberName lineTo bigDecimal.javaTyped
val Int.typed: Typed get() = typed(typedLine)

val String.typedLine: TypedLine get() = textName lineTo javaTyped
val String.typed: Typed get() = typed(typedLine)

val Expression.javaTyped: Typed get() = of(javaType)
val Expression.javaNumberTyped: Typed get() = typed(numberName lineTo of(javaType))
val Expression.javaTextTyped: Typed get() = typed(textName lineTo of(javaType))

fun Typed.plus(typed: TypedChoice): Typed =
	add(expression, type.isStatic, typed.expression, typed.choice.isStatic) of type.plus(typed.choice)

fun TypedRepeating.plus(typed: TypedChoice): TypedRepeating? =
	notNullIf(repeating.choice == typed.choice) {
		expression.term.append(typed.expression.term).dynamicExpression.of(repeating)
	}

fun Typed.plus(typed: TypedLine): Typed = plus(typed.choice)
fun Typed.plus(name: String): Typed = plus(name lineTo emptyTyped)

fun Expression.typedOf(typeLink: TypeLink): TypedLink =
	pair(typeLink.lhs.isStatic, typeLink.choice.isStatic).let { expressionPair ->
		expressionPair.first.of(typeLink.lhs) linkTo expressionPair.second.of(typeLink.choice)
	}

val Typed.linkOrNull: TypedLink?
	get() = expression.applyOrNull(type.linkOrNull) { typedOf(it) }

val TypedLink.typed: Typed get() = lhs.plus(choice)

infix fun String.fieldTo(typed: Typed): TypedField =
	typed.expression of fieldTo(typed.type)

infix fun String.lineTo(typed: Typed): TypedLine =
	typed.expression of lineTo(typed.type)

val TypedField.rhs: Typed get() = expression.of(field.rhs)
val TypedLine.fieldOrNull: TypedField? get() = typeLine.fieldOrNull?.let { expression.of(it) }
val TypedLine.arrowOrNull: TypedArrow? get() = typeLine.arrowOrNull?.let { expression.of(it) }
val TypedLine.isJava: Boolean get() = typeLine.isJava
val TypedLine.rhsOrNull: Typed? get() = fieldOrNull?.rhs
val Typed.onlyChoiceOrNull: TypedChoice?
	get() =
		linkOrNull?.run { notNullIf(lhs.type.isEmpty) { choice } }
val Typed.repeatingOrNull: TypedRepeating? get() = expression.applyOrNull(type.repeatingOrNull) { of(it) }
val Typed.onlyLineOrNull: TypedLine? get() = onlyChoiceOrNull?.onlyLineOrNull
val Typed.rhsOrNull: Typed? get() = onlyLineOrNull?.fieldOrNull?.rhs
val TypedChoice.onlyLineOrNull: TypedLine?
	get() =
		choice.onlyLineOrNull?.let { typeLine ->
			expression of typeLine
		}
val TypedLine.choice: TypedChoice
	get() =
		expression of typeLine.choice
val TypedArrow.line: TypedLine get() = expression.of(arrow.line)
val TypedField.line: TypedLine get() = expression.of(field.line)
val TypedRepeating.typed: Typed get() = expression.of(repeating.toType)

fun typed(line: TypedLine, vararg lines: TypedLine): Typed =
	emptyTyped.plus(line).fold(lines) { plus(it) }

fun typed(name: String, vararg names: String): Typed =
	emptyTyped.plus(name).fold(names) { plus(it) }

//fun TypedLine.of(choice: Choice): TypedChoice? =
//	of(choice, 0, null)
//

//fun TypedLine.of(choice: Choice, size: Int, indexOrNull: Int?): TypedChoice? =
//	when (choice) {
//		is LineChoice -> indexOrNull?.let { index -> choiceTerm(size, index, expression).dynamicExpression of choice }
//		is LinkChoice -> of(choice.link, size, indexOrNull)
//	}
//
//fun TypedLine.of(link: ChoiceLink, size: Int, indexOrNull: Int?): TypedChoice? =
//	of(link.lhs, size.inc(), if (indexOrNull == null && typeLine == link.line) 0 else indexOrNull)
//
//fun Match.matchPlus(case: Case): Match? =
//	when (typeLineStack) {
//		is EmptyStack -> null
//		is LinkStack ->
//			if (typeLineStack.link.value.name != case.name) null
//			else if (typeOrNull != null && case.typed.type != typeOrNull) null
//			else Match(typeLineStack.link.stack, expression.invoke(case.typed.expression), case.typed.type)
//	}
//
//val Match.typedOrNull: Typed?
//	get() =
//		when (typeLineStack) {
//			is EmptyStack -> expression.applyOrNull(typeOrNull) { of(it) }
//			is LinkStack -> null
//		}

val Typed.eval: Typed
	get() =
		expression.eval of type

val Typed.asDynamic: Typed
	get() =
		expression.asDynamic of type

fun Typed.applyValue(rhs: Typed, fn: Any?.(Any?) -> Any?): Typed =
	expression.applyValue(rhs.expression, fn) of javaType

fun Typed.updateExpression(fn: Expression.() -> Expression): Typed =
	copy(expression = expression.fn())

val Typed.withFnTerm
	get() =
		fn(expression.term).dynamicExpression of type

val Typed.staticScriptOrNull: Script?
	get() =
		notNullIf(type.isStatic) { type.script }