package leo25

import leo.base.orNull
import leo.base.runLet

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
		is StructValue -> resolutionOrNull(value.struct)
		is FunctionValue -> null
		is StringValue -> resolutionOrNull(token(end(value.string)))
		is WordValue -> resolutionOrNull(value.word)
	}

fun Context.resolutionOrNull(struct: Struct): Resolution? =
	null
		?: staticResolutionOrNull(struct)
		?: dynamicResolutionOrNull(struct)

fun Context.staticResolutionOrNull(struct: Struct): Resolution? =
	null

fun Context.dynamicResolutionOrNull(struct: Struct): Resolution? =
	orNull
		?.resolutionOrNull(struct.head)
		?.contextOrNull
		?.runLet(struct.tail) { tail ->
			if (tail == null) resolutionOrNull(token(emptyEnd))
			else resolutionOrNull(tail)
		}

fun Context.resolutionOrNull(field: Field): Resolution? =
	orNull
		?.resolutionOrNull(BeginToken(Begin(field.word)))
		?.contextOrNull
		?.resolutionOrNull(field.value)

fun Context.resolutionOrNull(word: Word): Resolution? =
	orNull
		?.resolutionOrNull(BeginToken(Begin(word)))
		?.contextOrNull
		?.resolutionOrNull(EndToken(EmptyEnd))
		?.contextOrNull
		?.resolutionOrNull(EndToken(EmptyEnd))

val Resolution.contextOrNull get() = (this as? ContextResolution)?.context
val Resolution.bindingOrNull get() = (this as? BindingResolution)?.binding

fun Context.plusGiven(value: Value): Context =
	plus(
		value("given" to null),
		binding(value("given" to value))
	)
