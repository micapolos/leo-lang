package leo25

import kotlinx.collections.immutable.persistentMapOf
import leo.base.orNull
import leo.base.runIfNotNull

fun Context.resolve(value: Value): Value =
	null
		?: applyOrNull(value)
		?: value

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
		is LinkValue -> resolutionOrNull(value.link)
		is FunctionValue -> null
		is StringValue -> null
		is WordValue -> resolutionOrNull(value.word)
	}

fun Context.resolutionOrNull(link: Link): Resolution? =
	null
		?: staticResolutionOrNull(link)
		?: dynamicResolutionOrNull(link)

fun Context.staticResolutionOrNull(link: Link): Resolution? =
	TODO()

fun Context.dynamicResolutionOrNull(link: Link): Resolution? =
	orNull
		?.resolutionOrNull(link.head)
		?.contextOrNull
		?.runIfNotNull(link.tail) { tail ->
			resolutionOrNull(tail)
		}

fun Context.resolutionOrNull(line: Line): Resolution? =
	orNull
		?.resolutionOrNull(BeginToken(Begin(line.word)))
		?.contextOrNull
		?.resolutionOrNull(line.value)

fun Context.resolutionOrNull(word: Word): Resolution? =
	orNull
		?.resolutionOrNull(BeginToken(Begin(word)))
		?.contextOrNull
		?.resolutionOrNull(EndToken(EmptyEnd))

val Resolution.contextOrNull get() = (this as? ContextResolution)?.context
val Resolution.bindingOrNull get() = (this as? BindingResolution)?.binding

fun Function.apply(value: Value): Value =
	context.give(value).resolve(body.value)

fun Context.give(value: Value): Context =
	Context(
		tokenToResolutionMap
			.put(
				BeginToken(Begin(Word("given"))),
				ContextResolution(
					Context(
						persistentMapOf(
							EndToken(EmptyEnd) to ContextResolution(
								Context(
									persistentMapOf(
										EndToken(EmptyEnd) to BindingResolution(
											ThingBinding(value)
										)
									)
								)
							)
						)
					)
				)
			)
	)

fun Binding.apply(value: Value): Value =
	when (this) {
		is FunctionBinding -> apply(value)
		is ThingBinding -> this.value
	}