package leo14.type.value

import leo14.lambda.Term
import leo14.lambda.id
import leo14.type.*
import leo14.typed.plus

infix fun <T> Term<T>.of(thunk: TypeThunk) = Value(this, thunk)
infix fun <T> Term<T>.of(thunk: NativeThunk) = NativeValue(this, thunk)
infix fun <T> Term<T>.of(thunk: StructureThunk) = StructureValue(this, thunk)
infix fun <T> Term<T>.of(thunk: ListThunk) = ListValue(this, thunk)
infix fun <T> Term<T>.of(thunk: ChoiceThunk) = ChoiceValue(this, thunk)
infix fun <T> Term<T>.of(thunk: ActionThunk) = ActionValue(this, thunk)
infix fun <T> Term<T>.of(thunk: RecursiveThunk) = RecursiveValue(this, thunk)
infix fun <T> Term<T>.of(thunk: FieldThunk) = FieldValue(this, thunk)

fun <T> StructureValue<T>.plus(fieldValue: FieldValue<T>): StructureValue<T> =
	plusTerm(fieldValue) of thunk.structure.plus(fieldValue.thunk.field).with(thunk.scope)

fun <T> StructureValue<T>.plusTerm(fieldValue: FieldValue<T>): Term<T> =
	if (thunk.isStatic)
		if (fieldValue.thunk.isStatic) id()
		else fieldValue.term
	else
		if (fieldValue.thunk.isStatic) term
		else term.plus(fieldValue.term)
