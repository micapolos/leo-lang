package leo14.type.value

import leo.base.notNullIf
import leo14.Dictionary
import leo14.lambda.first
import leo14.lambda.id
import leo14.lambda.second
import leo14.type.isStatic
import leo14.type.thunk.isEmpty
import leo14.type.thunk.make
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

val <T> StructureValue<T>.resolveOnlyFieldValueOrNull
	get() =
		resolveSplit?.run {
			notNullIf(first.thunk.isEmpty) {
				second
			}
		}

fun <T> StructureValue<T>.resolveLastFieldValueOrNull(name: String): FieldValue<T>? =
	resolveSplit?.run {
		if (second.thunk.field.name == name) second
		else first.resolveLastFieldValueOrNull(name)
	}

val <T> FieldValue<T>.resolveRhsValue
	get() =
		term of thunk.rhsThunk

val <T> Value<T>.resolveBody: Value<T>?
	get() =
		structureValueOrNull
			?.resolveOnlyFieldValueOrNull
			?.rhsValue

fun <T> Value<T>.resolveGet(name: String): Value<T>? =
	resolveBody
		?.structureValueOrNull
		?.resolveLastFieldValueOrNull(name)
		?.structureValue
		?.typeValue

fun <T> Value<T>.resolveMake(name: String): Value<T>? =
	term of thunk.make(name)

fun <T> Value<T>.resolveLast(dictionary: Dictionary): Value<T>? =
	resolveBody
		?.structureValueOrNull
		?.resolveLastFieldValueOrNull
		?.structureValue
		?.typeValue
		?.resolveMake(dictionary.last)

fun <T> Value<T>.resolvePrevious(dictionary: Dictionary): Value<T>? =
	resolveBody
		?.structureValueOrNull
		?.resolvePreviousValueOrNull
		?.typeValue
		?.resolveMake(dictionary.previous)
