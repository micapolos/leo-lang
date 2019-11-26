package leo14.type.value

import leo14.lambda.first
import leo14.lambda.id
import leo14.lambda.second
import leo14.type.isStatic
import leo14.type.thunk.rhsThunk
import leo14.type.thunk.split

val <T> StructureValue<T>.resolveSplit: Pair<StructureValue<T>, FieldValue<T>>?
	get() =
		thunk
			.split
			?.let { (structureThunk, fieldThunk) ->
				if (structureThunk.isStatic)
					if (fieldThunk.isStatic) id<T>().of(structureThunk) to id<T>().of(fieldThunk)
					else id<T>().of(structureThunk) to term.of(fieldThunk)
				else
					if (fieldThunk.isStatic) term.of(structureThunk) to id<T>().of(fieldThunk)
					else term.first.of(structureThunk) to term.second.of(fieldThunk)
			}

val <T> StructureValue<T>.resolvePreviousValueOrNull
	get() =
		resolveSplit?.first

val <T> StructureValue<T>.resolveLastFieldValueOrNull
	get() =
		resolveSplit?.second

val <T> FieldValue<T>.resolveRhsValue
	get() =
		term of thunk.rhsThunk
