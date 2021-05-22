package leo25

import leo.base.orNull
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
		?: resolutionOrNull(EndToken(AnythingEnd))

fun Dictionary.concreteResolutionOrNull(value: Value): Resolution? =
	when (value) {
		EmptyValue -> resolutionOrNull(token(emptyEnd))
		is LinkValue -> resolutionOrNull(value.link)
	}

fun Dictionary.resolutionOrNull(link: Link): Resolution? =
	orNull
		?.resolutionOrNull(link.head)
		?.dictionaryOrNull
		?.resolutionOrNull(link.tail)

fun Dictionary.resolutionOrNull(line: Line): Resolution? =
	when (line) {
		is FieldLine -> resolutionOrNull(line.field)
		is FunctionLine -> resolutionOrNull(line.function)
		is NativeLine -> resolutionOrNull(line.native)
	}

fun Dictionary.resolutionOrNull(function: Function): Resolution? =
	resolutionOrNull(token(begin(doingName)))?.dictionaryOrNull?.resolutionOrNull(token(anyEnd))

fun Dictionary.resolutionOrNull(field: Field): Resolution? =
	orNull
		?.resolutionOrNull(BeginToken(Begin(field.name)))
		?.dictionaryOrNull
		?.resolutionOrNull(field.value)

fun Dictionary.resolutionOrNull(native: Native): Resolution? =
	resolutionOrNull(token(native))

val Resolution.dictionaryOrNull get() = (this as? DictionaryResolution)?.dictionary
val Resolution.bindingOrNull get() = (this as? BindingResolution)?.binding

fun Dictionary.plusGiven(value: Value): Dictionary =
	when (value) {
		EmptyValue -> this
		is LinkValue -> plusGiven(value.link)
	}

fun Dictionary.plusGiven(link: Link): Dictionary =
	plusGiven(link.tail).plusGiven(link.head)

fun Dictionary.plusGiven(line: Line): Dictionary =
	plus(
		script(getName lineTo script(line.selectName)),
		binding(value(line))
	)