package leo14.type.value

import leo14.lambda.Term
import leo14.lambda.id
import leo14.lambda.pair
import leo14.type.isStatic
import leo14.type.plus
import leo14.type.thunk.*
import leo14.typed.plus

infix fun <T> Term<T>.of(thunk: TypeThunk) = Value(this, thunk)
infix fun <T> Term<T>.of(thunk: NativeThunk) = NativeValue(this, thunk)
infix fun <T> Term<T>.of(thunk: StructureThunk) = StructureValue(this, thunk)
infix fun <T> Term<T>.of(thunk: ListThunk) = ListValue(this, thunk)
infix fun <T> Term<T>.of(thunk: ChoiceThunk) = ChoiceValue(this, thunk)
infix fun <T> Term<T>.of(thunk: ActionThunk) = ActionValue(this, thunk)
infix fun <T> Term<T>.of(thunk: RecursiveThunk) = RecursiveValue(this, thunk)
infix fun <T> Term<T>.of(thunk: FieldThunk) = FieldValue(this, thunk)

infix fun <T> TypeThunk.value(term: Term<T>) = term of this
infix fun <T> NativeThunk.value(term: Term<T>) = term of this
infix fun <T> StructureThunk.value(term: Term<T>) = term of this
infix fun <T> ListThunk.value(term: Term<T>) = term of this
infix fun <T> ChoiceThunk.value(term: Term<T>) = term of this
infix fun <T> ActionThunk.value(term: Term<T>) = term of this
infix fun <T> RecursiveThunk.value(term: Term<T>) = term of this
infix fun <T> FieldThunk.value(term: Term<T>) = term of this

fun <T> StructureValue<T>.plus(fieldValue: FieldValue<T>): StructureValue<T> =
	plusTerm(fieldValue) of thunk.structure.plus(fieldValue.thunk.field).with(thunk.scope)

fun <T> StructureValue<T>.plusTerm(fieldValue: FieldValue<T>): Term<T> =
	if (thunk.isStatic)
		if (fieldValue.thunk.isStatic) id()
		else fieldValue.term
	else
		if (fieldValue.thunk.isStatic) term
		else term.plus(fieldValue.term)

val <T> StructureValue<T>.split: Pair<StructureValue<T>, FieldValue<T>>?
	get() =
		thunk
			.split
			?.let { (previousThunk, lastThunk) ->
				if (previousThunk.isStatic)
					if (lastThunk.isStatic) id<T>().of(previousThunk) to id<T>().of(lastThunk)
					else id<T>().of(previousThunk) to term.of(lastThunk)
				else
					if (lastThunk.isStatic) term.of(previousThunk) to id<T>().of(lastThunk)
					else term.pair().let { (previousTerm, lastTerm) ->
						previousTerm.of(previousThunk) to lastTerm.of(lastThunk)
					}
			}

val <T> Value<T>.structureValueOrNull
	get() =
		thunk.structureThunkOrNull?.run { term of this }

val <T> Value<T>.listValueOrNull
	get() =
		thunk.listThunkOrNull?.run { term of this }

val <T> StructureValue<T>.lastFieldValueOrNull
	get() =
		split?.second

val <T> StructureValue<T>.previousValueOrNull
	get() =
		split?.first

val <T> FieldValue<T>.rhsValue
	get() =
		term of thunk.rhsThunk

val <T> FieldValue<T>.structureValue
	get() =
		term of thunk.structureThunk

val <T> StructureValue<T>.typeValue
	get() =
		term of thunk.typeThunk
