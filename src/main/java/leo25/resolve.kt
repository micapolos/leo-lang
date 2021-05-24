package leo25

import leo.base.effect
import leo.base.fold
import leo.base.orNull
import leo.base.reverse
import leo14.lineTo
import leo14.script

fun Resolver.resolveLeo(value: Value): Leo<Value> =
	applyOrNullLeo(value).or {
		value.resolveLeo
	}

fun Resolver.applyOrNullLeo(value: Value): Leo<Value?> =
	resolutionOrNull(value)?.bindingOrNull?.applyLeo(value) ?: leo(null)

fun Resolver.resolutionOrNull(token: Token): Resolution? =
	tokenToResolutionMap[token]

fun Resolver.resolutionOrNull(value: Value): Resolution? =
	null
		?: concreteResolutionOrNull(value)
		?: resolutionOrNull(token(anyEnd))

fun Resolver.concreteResolutionOrNull(value: Value): Resolution? =
	when (value) {
		is EmptyValue -> resolutionOrNull(token(emptyEnd))
		is LinkValue -> resolutionOrNull(value.link)
	}

fun Resolver.resolutionOrNull(link: Link): Resolution? =
	orNull
		?.resolutionOrNull(link.field)
		?.dictionaryOrNull
		?.resolutionOrNull(link.value)

fun Resolver.resolutionOrNull(function: Function): Resolution? =
	null

fun Resolver.resolutionOrNull(field: Field): Resolution? =
	orNull
		?.resolutionOrNull(token(begin(field.name)))
		?.dictionaryOrNull
		?.resolutionOrNull(field.rhs)

fun Resolver.resolutionOrNull(rhs: Rhs): Resolution? =
	when (rhs) {
		is ValueRhs -> resolutionOrNull(rhs.value)
		is FunctionRhs -> resolutionOrNull(rhs.function)
		is NativeRhs -> resolutionOrNull(rhs.native)
	} ?: resolutionOrNull(token(anyEnd))

fun Resolver.resolutionOrNull(native: Native): Resolution? =
	resolutionOrNull(token(native))

val Resolution.dictionaryOrNull get() = (this as? ResolverResolution)?.resolver
val Resolution.bindingOrNull get() = (this as? BindingResolution)?.binding

fun Resolver.set(value: Value): Resolver =
	fold(value.fieldSeq.reverse) { set(it) }

fun Resolver.set(line: Field): Resolver =
	plus(
		definition(
			pattern(script(getName lineTo script(line.name))),
			binding(value(line))
		)
	)