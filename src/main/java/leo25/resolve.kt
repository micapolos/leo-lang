package leo25

import leo.base.fold
import leo.base.orNull
import leo.base.reverse
import leo14.script

fun Dictionary.resolveLeo(value: Value): Leo<Value> =
	value.tracedLeo.bind {
		applyOrNullLeo(value).or {
			value.resolveLeo
		}
	}

fun Dictionary.applyOrNullLeo(value: Value): Leo<Value?> =
	resolutionOrNull(value)?.bindingOrNull?.applyLeo(value) ?: leo(null)

inline fun Dictionary.resolutionOrNull(token: Token): Resolution? =
	tokenToResolutionMap.get(token)

fun Dictionary.resolutionOrNull(value: Value): Resolution? =
	null
		?: concreteResolutionOrNull(value)
		?: resolutionOrNull(token(anyEnd))

inline fun Dictionary.concreteResolutionOrNull(value: Value): Resolution? =
	when (value) {
		is EmptyValue -> resolutionOrNull(token(emptyEnd))
		is LinkValue -> resolutionOrNull(value.link)
	}

inline fun Dictionary.resolutionOrNull(link: Link): Resolution? =
	orNull
		?.resolutionOrNull(link.field)
		?.dictionaryOrNull
		?.resolutionOrNull(link.value)

inline fun Dictionary.resolutionOrNull(function: Function): Resolution? =
	null

inline fun Dictionary.resolutionOrNull(field: Field): Resolution? =
	orNull
		?.resolutionOrNull(token(begin(field.name)))
		?.dictionaryOrNull
		?.resolutionOrNull(field.rhs)

inline fun Dictionary.resolutionOrNull(rhs: Rhs): Resolution? =
	when (rhs) {
		is ValueRhs -> resolutionOrNull(rhs.value)
		is FunctionRhs -> resolutionOrNull(rhs.function)
		is NativeRhs -> resolutionOrNull(rhs.native)
	} ?: resolutionOrNull(token(anyEnd))

inline fun Dictionary.resolutionOrNull(native: Native): Resolution? =
	resolutionOrNull(token(native))

inline val Resolution.dictionaryOrNull get() = (this as? ResolverResolution)?.dictionary
inline val Resolution.bindingOrNull get() = (this as? BindingResolution)?.binding

fun Dictionary.set(value: Value): Dictionary =
	fold(value.fieldSeq.reverse) { set(it) }

fun Dictionary.set(line: Field): Dictionary =
	plus(
		definition(
			pattern(script(line.name)),
			binding(value(line))
		)
	)