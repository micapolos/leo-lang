package leo.base

data class Optional<T>(
	val orNull: T?)

val <T : Any> T?.optional
	get() =
		Optional(this)

val <T : Any> T.present
	get() =
		Optional(this)

fun <T : Any> absent() =
	Optional<T>(null)

fun <T : Any, R : Any> Optional<T>.ifPresent(fn: T.() -> R): Optional<R> =
	orNull?.fn().optional

fun <T : Any> Optional<T>.ifAbsent(fn: () -> T): T =
	orNull ?: fn()

fun <T : Any, R : Any> Optional<T>.ifPresentOptional(fn: T.() -> Optional<R>): Optional<R> =
	orNull?.fn() ?: absent()

