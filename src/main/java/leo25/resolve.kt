package leo25

import kotlinx.collections.immutable.persistentMapOf
import leo.base.orNull

fun Context.resolve(value: Value): Value =
	null
		?: applyOrNull(value)
		?: value

fun Context.applyOrNull(value: Value): Value? =
	resolutionOrNull(value)?.bindingOrNull?.apply(value)

fun Context.resolutionOrNull(token: Token): Resolution? =
	tokenToResolutionMap[token]

fun Context.resolutionOrNull(value: Value): Resolution? =
	when (value) {
		EmptyValue -> resolutionOrNull(EndToken(EmptyEnd))
		is ThingValue -> resolutionOrNull(value.thing)
	}

fun Context.resolutionOrNull(thing: Thing): Resolution? =
	null
		?: concreteResolutionOrNull(thing)
		?: resolutionOrNull(EndToken(AnythingEnd))

fun Context.concreteResolutionOrNull(thing: Thing): Resolution? =
	when (thing) {
		is LinkThing -> resolutionOrNull(thing.link)
		is FunctionThing -> null
		is StringThing -> null
		is WordThing -> resolutionOrNull(thing.word)
	}

fun Context.resolutionOrNull(link: Link): Resolution? =
	null
		?: staticResolutionOrNull(link)
		?: dynamicResolutionOrNull(link)

fun Context.staticResolutionOrNull(link: Link): Resolution? =
	TODO()

fun Context.dynamicResolutionOrNull(link: Link): Resolution? =
	orNull
		?.resolutionOrNull(link.line)
		?.contextOrNull
		?.resolutionOrNull(link.value)

fun Context.resolutionOrNull(line: Line): Resolution? =
	orNull
		?.resolutionOrNull(BeginToken(Begin(line.word)))
		?.contextOrNull
		?.resolutionOrNull(line.thing)

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
											ValueBinding(value)
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
		is ValueBinding -> this.value
	}