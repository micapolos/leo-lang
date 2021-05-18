package leo25

import kotlinx.collections.immutable.PersistentMap
import leo.base.orIfNull

data class Context(val tokenToResolutionMap: PersistentMap<Token, Resolution>)

sealed class Token
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()

data class Begin(val word: Word) : Token()

sealed class End
object EmptyEnd : End()
object AnythingEnd : End()
data class StringEnd(val string: String) : End()

sealed class Resolution
data class ContextResolution(val context: Context) : Resolution()
data class BindingResolution(val binding: Binding) : Resolution()

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)

fun begin(word: Word) = Begin(word)
val emptyEnd: End = EmptyEnd
val anyEnd: End = AnythingEnd
fun end(string: String): End = StringEnd(string)

fun resolution(context: Context): Resolution = ContextResolution(context)
fun resolution(binding: Binding): Resolution = BindingResolution(binding)

fun Context.update(token: Token, fn: (Resolution?) -> Resolution?): Context =
	fn(tokenToResolutionMap[token]).let { resolutionOrNull ->
		Context(
			if (resolutionOrNull == null) tokenToResolutionMap.remove(token)
			else tokenToResolutionMap.put(token, resolutionOrNull)
		)
	}

fun Context.updateContinuation(token: Token, fn: Context.() -> Resolution): Context =
	update(token) { resolutionOrNull ->
		resolutionOrNull?.continuationContext.orIfNull { context() }.fn()
	}

fun Context.plus(value: Value, resolution: Resolution): Context =
	when (value) {
		is FunctionValue -> TODO()
		is LinkValue -> TODO()
		is StringValue ->
			updateContinuation(token(end(value.string))) {
				resolution
			}
		is WordValue ->
			updateContinuation(token(begin(value.word))) {
				resolution(updateContinuation(token(emptyEnd)) {
					resolution(updateContinuation(token(emptyEnd)) {
						resolution
					})
				})
			}
	}

fun Context.plus(word: Word, resolution: Resolution): Context =
	plusSpecial(word, resolution) ?: plusRaw(word, resolution)

fun Context.plusSpecial(word: Word, resolution: Resolution): Context? =
	when (word.string) {
		"any" -> plusAny(resolution)
		else -> null
	}

fun Context.plusAny(resolution: Resolution): Context =
	TODO()

fun Context.plusRaw(word: Word, resolution: Resolution): Context =
	updateContinuation(token(begin(word))) {
		resolution(updateContinuation(token(emptyEnd)) {
			resolution(updateContinuation(token(emptyEnd)) {
				resolution
			})
		})
	}

val Resolution.continuationContext: Context
	get() =
		when (this) {
			is BindingResolution -> context()
			is ContextResolution -> context
		}

