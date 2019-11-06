package leo14.typed

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.lambda.Term

infix fun <T> Typed<T>.castTo(toType: Type): Term<T>? =
	if (type == toType) term // TODO: This can quickly become quadratic. Maybe return (Term, didCast)?
	else resolveLinkOrNull
		?.let { typedLink ->
			toType.lineLinkOrNull?.let { typeLink ->
				typedLink.tail.castTo(typeLink.tail)?.let { castTail ->
					typedLink.head.castTo(typeLink.head)?.let { castHead ->
						castTail.plus(castHead)
					}
				}
			}
		}
		?: notNullIf(toType.isEmpty) { term }

infix fun <T> TypedLine<T>.castTo(toLine: Line): Term<T>? =
	when (line) {
		is NativeLine -> ifOrNull(line == toLine) { term }
		is ChoiceLine ->
			if (toLine is ChoiceLine) (term of line.choice).castTo(toLine.choice)
			else null
		is ArrowLine -> ifOrNull(line == toLine) { term }
	}

infix fun <T> TypedChoice<T>.castTo(toChoice: Choice): Term<T>? =
	choice.onlyFieldOrNull
		?.let { onlyField -> (term of onlyField).castTo(toChoice) }
		?: notNullIf(choice == toChoice) { term }

infix fun <T> TypedField<T>.castTo(toChoice: Choice): Term<T>? =
	toChoice.onlyFieldOrNull
		?.let { onlyField -> castTo(onlyField) }
		?: TODO()

infix fun <T> TypedField<T>.castTo(toField: Field): Term<T>? =
	ifOrNull(field.string == toField.string) {
		(term of field.rhs).castTo(toField.rhs)
	}
