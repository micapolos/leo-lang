package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.orIfNull

data class Context(val tokenToResolutionMap: PersistentMap<Token, Resolution>)

fun context() = Context(persistentMapOf())

sealed class Token
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()

data class Begin(val word: Word)

sealed class End
object EmptyEnd : End()
object AnythingEnd : End()
data class StringEnd(val string: String) : End()

sealed class Resolution
data class ContextResolution(val context: Context) : Resolution()
data class BindingResolution(val binding: Binding) : Resolution()

fun Context.put(token: Token, resolution: Resolution): Context =
	Context(tokenToResolutionMap.put(token, resolution))

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

fun Context.plus(string: String, resolution: Resolution): Context =
	updateContinuation(token(end(string))) {
		resolution
	}

// TODO: This is slow!!! Refactor Context to make removing for "any" fast,
// by separating non-any stuff into different structure.
val Context.removeForAny: Context
	get() =
		tokenToResolutionMap.entries.fold(context()) { context, (token, resolution) ->
			when (token) {
				is BeginToken -> context
				is EndToken ->
					when (token.end) {
						AnythingEnd -> context
						EmptyEnd -> context.put(token, resolution)
						is StringEnd -> context
					}
			}
		}

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

fun Context.plus(value: Value, binding: Binding): Context =
	update(value) {
		resolution(binding)
	}

fun Context.update(value: Value, fn: Context.() -> Resolution): Context =
	when (value) {
		is FunctionValue -> this // functions can not be used in pattern matching
		is LinkValue -> update(value.link, fn)
		is StringValue -> update(value.string, fn)
		is WordValue -> update(value.word, fn)
	}

fun Context.update(link: Link, fn: Context.() -> Resolution): Context =
	update(link.head) {
		resolution(
			if (link.tail == null) updateContinuation(token(emptyEnd), fn)
			else update(link.tail, fn)
		)
	}

fun Context.update(line: Line, fn: Context.() -> Resolution): Context =
	updateContinuation(token(begin(line.word))) {
		resolution(update(line.value, fn))
	}

fun Context.update(word: Word, fn: Context.() -> Resolution): Context =
	updateKeywordOrNull(word, fn) ?: updateExact(word, fn)

fun Context.updateKeywordOrNull(word: Word, fn: Context.() -> Resolution): Context? =
	when (word.string) {
		"any" -> updateAny(fn)
		else -> null
	}

fun Context.updateAny(fn: Context.() -> Resolution): Context =
	removeForAny.updateContinuation(token(anyEnd)) { fn() }

fun Context.updateExact(word: Word, fn: Context.() -> Resolution): Context =
	updateContinuation(token(begin(word))) {
		resolution(updateContinuation(token(emptyEnd)) {
			resolution(updateContinuation(token(emptyEnd)) {
				fn()
			})
		})
	}

fun Context.update(string: String, fn: Context.() -> Resolution): Context =
	updateContinuation(token(end(string)), fn)