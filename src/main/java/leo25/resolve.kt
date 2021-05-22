package leo25

import leo.base.fold
import leo.base.orNull
import leo.base.orNullFold
import leo.base.reverse
import leo13.*
import leo14.lineTo
import leo14.script

fun Dictionary.resolve(value: Value): Value =
	null
		?: applyOrNull(value)
		?: value.resolve

fun Dictionary.applyOrNull(value: Value): Value? =
	resolutionOrNull(value)?.bindingOrNull?.apply(value)

fun Dictionary.resolutionOrNull(token: Token): Resolution? =
	tokenToResolutionMap[token]

fun Dictionary.resolutionOrNull(value: Value): Resolution? =
	null
		?: concreteResolutionOrNull(value)
		?: resolutionOrNull(token(anyEnd))

fun Dictionary.concreteResolutionOrNull(value: Value): Resolution? =
	when (value) {
		is EmptyValue -> resolutionOrNull(token(emptyEnd))
		is LinkValue -> resolutionOrNull(value.link)
	}

fun Dictionary.resolutionOrNull(link: Link): Resolution? =
	orNull
		?.resolutionOrNull(link.field)
		?.dictionaryOrNull
		?.resolutionOrNull(link.value)

fun Dictionary.resolutionOrNull(function: Function): Resolution? =
	null

fun Dictionary.resolutionOrNull(field: Field): Resolution? =
	orNull
		?.resolutionOrNull(token(begin(field.name)))
		?.dictionaryOrNull
		?.resolutionOrNull(field.rhs)

fun Dictionary.resolutionOrNull(rhs: Rhs): Resolution? =
	when (rhs) {
		is ValueRhs -> resolutionOrNull(rhs.value)
		is FunctionRhs -> resolutionOrNull(rhs.function)
		is NativeRhs -> resolutionOrNull(rhs.native)
	} ?: resolutionOrNull(token(anyEnd))

fun Dictionary.resolutionOrNull(native: Native): Resolution? =
	resolutionOrNull(token(native))

val Resolution.dictionaryOrNull get() = (this as? DictionaryResolution)?.dictionary
val Resolution.bindingOrNull get() = (this as? BindingResolution)?.binding

fun Dictionary.plusGiven(value: Value): Dictionary =
	fold(value.fieldSeq.reverse) { plusGiven(it) }

fun Dictionary.plusGiven(line: Field): Dictionary =
	plus(
		script(getName lineTo script(line.name)),
		binding(value(line))
	)