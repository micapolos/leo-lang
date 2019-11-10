package leo14.typed

import leo.base.*
import leo13.Index
import leo13.index0
import leo13.next
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke

sealed class Cast<T>

data class EmptyCast<T>(val empty: Empty) : Cast<T>()
data class TermCast<T>(val term: Term<T>) : Cast<T>()

fun <T> cast(empty: Empty): Cast<T> = EmptyCast(empty)
fun <T> cast(term: Term<T>): Cast<T> = TermCast(term)

fun <T> Cast<T>.resolve(input: Term<T>) =
	when (this) {
		is EmptyCast -> input
		is TermCast -> term
	}

infix fun <T> Typed<T>.castTypedTo(toType: Type): Typed<T> =
	castTermTo(toType).notNullOrError("$type as $toType") of toType

infix fun <T> Typed<T>.castTermTo(toType: Type): Term<T>? =
	castTo(toType)?.resolve(term)

infix fun <T> Typed<T>.castTo(toType: Type): Cast<T>? =
	resolveLinkOrNull
		?.let { typedLink ->
			toType.lineLinkOrNull?.let { typeLink ->
				typedLink.tail.castTo(typeLink.tail)?.let { tailCast ->
					typedLink.head.castTo(typeLink.head)?.let { headCast ->
						when (tailCast) {
							is EmptyCast ->
								when (headCast) {
									is EmptyCast -> cast(empty)
									is TermCast -> cast(typedLink.tail.plus(headCast.term of typeLink.head).term)
								}
							is TermCast ->
								when (headCast) {
									is EmptyCast -> cast(tailCast.term.plus(typedLink.head.term))
									is TermCast -> cast(tailCast.term.plus(headCast.term))
								}
						}
					}
				}
			}
		}
		?: notNullIf(toType.isEmpty) { cast<T>(empty) }

infix fun <T> TypedLine<T>.castTo(toLine: Line): Cast<T>? =
	when (line) {
		is NativeLine -> ifOrNull(line == toLine) { cast<T>(empty) }
		is FieldLine -> (term of line.field).castTo(toLine)
		is ChoiceLine -> ifOrNull(line == toLine) { cast<T>(empty) }
		is ArrowLine -> ifOrNull(line == toLine) { cast<T>(empty) }
	}

infix fun <T> TypedField<T>.castTo(toLine: Line): Cast<T>? =
	when (toLine) {
		is NativeLine -> null
		is FieldLine -> castTo(toLine.field)
		is ChoiceLine -> castTo(toLine.choice)
		is ArrowLine -> null
	}

infix fun <T> TypedField<T>.castTo(choice: Choice): Cast<T>? =
	castBody(choice, index0)?.bodyCast(choice)?.let { cast(it) }

fun <T> Term<T>.bodyCast(choice: Choice): Term<T> =
	choice.split { tailChoice, _ ->
		fn(bodyCast(tailChoice))
	} ?: this

fun <T> TypedField<T>.castBody(choice: Choice, index: Index): Term<T>? =
	choice.split { tailChoice, case ->
		castBody(case, index) ?: castBody(tailChoice, index.next)
	}

fun <T> TypedField<T>.castBody(case: Case, index: Index): Term<T>? =
	ifOrNull(field.string == case.string) {
		resolveRhs.let { resolvedRhs ->
			resolvedRhs.castTermTo(case.rhs)?.let { castRhsTerm ->
				arg<T>(index).invoke(castRhsTerm)
			}
		}
	}

infix fun <T> TypedField<T>.castTo(toField: Field): Cast<T>? =
	ifOrNull(field.string == toField.string) {
		(term of field.rhs).castTo(toField.rhs)
	}
