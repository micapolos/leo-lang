package leo25

import leo.base.orNull
import leo14.Literal
import leo14.script
import leo18.Tokenizer

fun Context.resolve(value: Value): Value =
	null
		?: applyOrNull(value)
		?: value.resolve

fun Context.applyOrNull(value: Value): Value? =
	resolutionOrNull(value)?.bindingOrNull?.apply(value)

fun Context.resolutionOrNull(token: Token): Resolution? =
	tokenToResolutionMap[token]

fun Context.resolutionOrNull(value: Value): Resolution? =
	null
		?: concreteResolutionOrNull(value)
		?: resolutionOrNull(EndToken(AnythingEnd))

fun Context.concreteResolutionOrNull(value: Value): Resolution? =
	when (value) {
		EmptyValue -> resolutionOrNull(token(emptyEnd))
		is LinkValue -> resolutionOrNull(value.link)
	}

fun Context.resolutionOrNull(link: Link): Resolution? =
	orNull
		?.resolutionOrNull(link.head)
		?.contextOrNull
		?.resolutionOrNull(link.tail)

fun Context.resolutionOrNull(line: Line): Resolution? =
	when (line) {
		is FieldLine -> resolutionOrNull(line.field)
		is FunctionLine -> resolutionOrNull(line.function)
		is NativeLine -> null
	}

fun Context.resolutionOrNull(function: Function): Resolution? =
	resolutionOrNull(token(begin(functionName)))?.contextOrNull?.resolutionOrNull(token(anyEnd))

fun Context.resolutionOrNull(literal: Literal): Resolution? =
	resolutionOrNull(token(end(literal)))

fun Context.resolutionOrNull(field: Field): Resolution? =
	orNull
		?.resolutionOrNull(BeginToken(Begin(field.name)))
		?.contextOrNull
		?.resolutionOrNull(field.value)

val Resolution.contextOrNull get() = (this as? ContextResolution)?.context
val Resolution.bindingOrNull get() = (this as? BindingResolution)?.binding

fun Context.plusGiven(value: Value): Context =
	plus(
		script(givenName),
		binding(value(givenName lineTo value))
	)
