package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.notNullIf
import leo.base.orIfNull
import leo14.*

data class Context(val tokenToResolutionMap: PersistentMap<Token, Resolution>)

fun context() = Context(persistentMapOf())

sealed class Token
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()
data class NativeToken(val native: Native) : Token()

data class Begin(val name: String)

sealed class End
object EmptyEnd : End()
object AnythingEnd : End()

sealed class Resolution
data class ContextResolution(val context: Context) : Resolution()
data class BindingResolution(val binding: Binding) : Resolution()

fun Context.put(token: Token, resolution: Resolution): Context =
	Context(tokenToResolutionMap.put(token, resolution))

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)
fun token(native: Native): Token = NativeToken(native)

fun begin(name: String) = Begin(name)
val emptyEnd: End = EmptyEnd
val anyEnd: End = AnythingEnd

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

fun Context.plus(literal: Literal, resolution: Resolution): Context =
	updateContinuation(token(begin(literal.selectName))) {
		resolution(updateContinuation(token(literal.native)) {
			resolution(updateContinuation(token(emptyEnd)) {
				resolution
			})
		})
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
					}
				is NativeToken -> context
			}
		}

fun Context.plusRaw(name: String, resolution: Resolution): Context =
	updateContinuation(token(begin(name))) {
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

fun Context.plus(script: Script, binding: Binding): Context =
	update(script) {
		resolution(binding)
	}

fun Context.update(script: Script, fn: Context.() -> Resolution): Context =
	null
		?: updateAnyOrNull(script, fn)
		?: updateExact(script, fn)

fun Context.updateExact(script: Script, fn: Context.() -> Resolution): Context =
	when (script) {
		is UnitScript -> updateContinuation(token(emptyEnd), fn)
		is LinkScript -> update(script.link, fn)
	}

fun Context.update(struct: ScriptLink, fn: Context.() -> Resolution): Context =
	update(struct.line) {
		resolution(
			update(struct.lhs, fn)
		)
	}

fun Context.update(line: ScriptLine, fn: Context.() -> Resolution): Context =
	when (line) {
		is FieldScriptLine -> update(line.field, fn)
		is LiteralScriptLine -> update(line.literal, fn)
	}

fun Context.update(literal: Literal, fn: Context.() -> Resolution): Context =
	updateContinuation(token(begin(literal.selectName))) {
		resolution(updateContinuation(token(literal.native)) {
			resolution(updateContinuation(token(emptyEnd), fn))
		})
	}

fun Context.updateAnyOrNull(script: Script, fn: Context.() -> Resolution): Context? =
	notNullIf(script == script(anyName)) {
		updateAny(fn)
	}

fun Context.update(field: ScriptField, fn: Context.() -> Resolution): Context =
	updateContinuation(token(begin(field.string))) {
		resolution(update(field.rhs, fn))
	}

fun Context.updateAny(fn: Context.() -> Resolution): Context =
	removeForAny.updateContinuation(token(anyEnd), fn)
