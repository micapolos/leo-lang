package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.*
import leo14.*
import leo14.matching.name

data class Context(val tokenToResolutionMap: PersistentMap<Token, Resolution>)

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

fun context(vararg pairs: Pair<Token, Resolution>): Context =
	Context(persistentMapOf()).fold(pairs) { put(it.first, it.second) }

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

val Context.removeForAny: Context
	get() =
		Context(
			tokenToResolutionMap[token(anyEnd)].let { resolutionOrNull ->
				if (resolutionOrNull == null) persistentMapOf()
				else persistentMapOf(token(anyEnd) to resolutionOrNull)
			})

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

operator fun Context.plus(context: Context): Context =
	runIf(context.resolutionOrNull(token(anyEnd)) != null) { removeForAny }
		.run {
			context.tokenToResolutionMap.entries.fold(this) { context, (token, resolution) ->
				context.update(token) { resolutionOrNull ->
					resolutionOrNull.orNullMerge(resolution)
				}
			}
		}

fun Resolution.merge(resolution: Resolution): Resolution =
	when (resolution) {
		is BindingResolution -> resolution
		is ContextResolution ->
			when (this) {
				is BindingResolution -> resolution
				is ContextResolution -> resolution(context.plus(resolution.context))
			}
	}

fun Resolution?.orNullMerge(resolution: Resolution): Resolution =
	this?.merge(resolution) ?: resolution

fun Context.switchOrNull(value: Value, script: Script): Value? =
	value.linkOrNull?.onlyLineOrNull?.fieldOrNull?.value?.let { switchBodyOrNull(it, script) }

fun Context.switchBodyOrNull(value: Value, script: Script): Value? =
	value.linkOrNull?.onlyLineOrNull?.let {
		switchOrNull(it, script)
	}

fun Context.switchOrNull(line: Line, script: Script): Value? =
	when (script) {
		is LinkScript -> switchOrNull(line, script.link)
		is UnitScript -> null
	}

fun Context.switchOrNull(line: Line, scriptLink: ScriptLink): Value? =
	switchOrNull(line, scriptLink.lhs) ?: switchOrNull(line, scriptLink.line)

fun Context.switchOrNull(line: Line, scriptLine: ScriptLine): Value? =
	when (scriptLine) {
		is FieldScriptLine -> switchOrNull(line, scriptLine.field)
		is LiteralScriptLine -> null
	}

fun Context.switchOrNull(line: Line, scriptField: ScriptField): Value? =
	ifOrNull(line.selectName == scriptField.name) {
		line.selectValueOrNull?.let { given ->
			plusGiven(given).interpretedValue(scriptField.rhs)
		}
	}

fun Context.apply(block: Block, given: Value): Value =
	when (block.typeOrNull) {
		BlockType.REPEATEDLY -> applyRepeating(block.untypedScript, given)
		BlockType.RECURSIVELY -> applyRecursing(block.untypedScript, given)
		null -> applyUntyped(block.untypedScript, given)
	}

tailrec fun Context.applyRepeating(script: Script, given: Value): Value {
	val result = plusGiven(given).interpretedValue(script)
	val repeatValue = result.repeatValueOrNull
	return if (repeatValue != null) applyRepeating(script, repeatValue)
	else result
}

fun Context.applyRecursing(script: Script, given: Value): Value =
	plusGiven(given).plusRecurse(script).interpretedValue(script)

fun Context.applyUntyped(script: Script, given: Value): Value =
	plusGiven(given).interpretedValue(script)

fun Context.plusRecurse(script: Script): Context =
	plus(
		script(
			anyName lineTo script(),
			recurseName lineTo script()
		),
		binding(context().function(body(BlockType.RECURSIVELY.block(script))))
	)