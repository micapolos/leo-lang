package leo15.type

import leo.base.*
import leo15.lambda.choiceTerm

val castRecursiveParameter: Parameter<Recursive?> = parameter(null)

sealed class Cast
object IdentityCast : Cast()
data class ExpressionCast(val expression: Expression) : Cast()

val identityCast: Cast = IdentityCast
val Expression.cast: Cast get() = ExpressionCast(this)

fun Cast.term(input: Expression): Expression =
	when (this) {
		IdentityCast -> input
		is ExpressionCast -> expression
	}

fun Typed.cast(to: Type): Typed? =
	when (to) {
		EmptyType -> notNullIf(isEmpty) { this }
		is LinkType -> linkOrNull?.cast(to.link)
		is RepeatingType -> cast(to.repeating)
		is RecursiveType -> cast(to.recursive)
		RecurseType -> castRecurse
	}

fun TypedLink.cast(to: TypeLink): Typed? =
	lhs.cast(to.lhs).orNullApply(choice.cast(to.choice), Typed::plus)

fun TypedChoice.cast(to: Choice): TypedChoice? =
	onlyLineOrNull
		?.run { cast(to) }
		?: exactCast(to)

fun TypedLine.cast(to: Choice): TypedChoice? =
	when (to) {
		is LineChoice -> notNullIf(typeLine == to.line) { choice }
		is LinkChoice -> cast(to.link, to, to.lineCount, 0)
	}

fun TypedLine.cast(to: Choice, choice: Choice, size: Int, index: Int): TypedChoice? =
	when (to) {
		is LineChoice ->
			cast(to.line)
				?.let { choiceTerm(size, index, it.expression.term).dynamicExpression.of(choice) }
		is LinkChoice -> cast(to.link, choice, size, index)
	}

fun TypedLine.cast(to: ChoiceLink, choice: Choice, size: Int, index: Int): TypedChoice? =
	cast(to.line)
		?.let { choiceTerm(size, index, it.expression.term).dynamicExpression.of(choice) }
		?: cast(to.lhs, choice, size, index.inc())

fun TypedChoice.exactCast(to: Choice): TypedChoice? =
	notNullIf(choice == to) { this }

fun TypedLine.cast(to: TypeLine): TypedLine? =
	when (to) {
		is LiteralTypeLine -> TODO()
		is FieldTypeLine -> fieldOrNull?.cast(to.field)
		is ArrowTypeLine -> arrowOrNull?.cast(to.arrow)
		JavaTypeLine -> notNullIf(isJava) { this }
	}

fun TypedField.cast(to: TypeField): TypedLine? =
	ifOrNull(field.name == to.name) {
		to.name.applyOrNull(rhs.cast(to.rhs)) { rhs ->
			fieldTo(rhs).line
		}
	}

fun TypedArrow.cast(to: Arrow): TypedLine? =
	notNullIf(arrow == to) { line }

fun Typed.cast(to: Repeating): Typed? =
	repeatingOrNull.let { repeating ->
		if (repeating == null) castRepeating(to)
		else repeating.cast(to)
	}?.typed

fun TypedRepeating.cast(to: Repeating): TypedRepeating? =
	notNullIf(repeating.choice == to.choice) { this }

fun Typed.castRepeating(to: Repeating): TypedRepeating? =
	when (type) {
		EmptyType -> expression.of(to)
		is LinkType -> linkOrNull?.castRepeating(to)
		is RepeatingType -> null
		is RecursiveType -> null
		RecurseType -> null
	}

fun TypedLink.castRepeating(to: Repeating): TypedRepeating? =
	choice.cast(to.choice)?.let { cast ->
		lhs.castRepeating(to)?.plus(cast)
	}

fun Typed.cast(to: Recursive): Typed? =
	type.recursiveOrNull
		?.let { TODO() }
		?: TODO()

val Typed.castRecurse: Typed?
	get() =
		cast(castRecursiveParameter.value!!.type)
