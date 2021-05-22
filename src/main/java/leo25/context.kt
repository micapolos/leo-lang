package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.*
import leo14.*
import leo14.matching.name

data class Dictionary(val tokenToResolutionMap: PersistentMap<Token, Resolution>)

sealed class Token
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()
data class NativeToken(val native: Native) : Token()

data class Begin(val name: String)

sealed class End
object EmptyEnd : End()
object AnythingEnd : End()

sealed class Resolution
data class DictionaryResolution(val dictionary: Dictionary) : Resolution()
data class BindingResolution(val binding: Binding) : Resolution()

fun Dictionary.put(token: Token, resolution: Resolution): Dictionary =
	Dictionary(tokenToResolutionMap.put(token, resolution))

fun dictionary(vararg pairs: Pair<Token, Resolution>): Dictionary =
	Dictionary(persistentMapOf()).fold(pairs) { put(it.first, it.second) }

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)
fun token(native: Native): Token = NativeToken(native)

fun begin(name: String) = Begin(name)
val emptyEnd: End = EmptyEnd
val anyEnd: End = AnythingEnd

fun resolution(dictionary: Dictionary): Resolution = DictionaryResolution(dictionary)
fun resolution(binding: Binding): Resolution = BindingResolution(binding)

fun Dictionary.update(token: Token, fn: (Resolution?) -> Resolution?): Dictionary =
	fn(tokenToResolutionMap[token]).let { resolutionOrNull ->
		Dictionary(
			if (resolutionOrNull == null) tokenToResolutionMap.remove(token)
			else tokenToResolutionMap.put(token, resolutionOrNull)
		)
	}

fun Dictionary.updateContinuation(token: Token, fn: Dictionary.() -> Resolution): Dictionary =
	update(token) { resolutionOrNull ->
		resolutionOrNull?.continuationDictionary.orIfNull { dictionary() }.fn()
	}

fun Dictionary.plus(literal: Literal, resolution: Resolution): Dictionary =
	updateContinuation(token(begin(literal.selectName))) {
		resolution(updateContinuation(token(literal.native)) {
			resolution(updateContinuation(token(emptyEnd)) {
				resolution
			})
		})
	}

val Dictionary.removeForAny: Dictionary
	get() =
		Dictionary(
			tokenToResolutionMap[token(anyEnd)].let { resolutionOrNull ->
				if (resolutionOrNull == null) persistentMapOf()
				else persistentMapOf(token(anyEnd) to resolutionOrNull)
			})

val Resolution.continuationDictionary: Dictionary
	get() =
		when (this) {
			is BindingResolution -> dictionary()
			is DictionaryResolution -> dictionary
		}

fun Dictionary.plus(script: Script, binding: Binding): Dictionary =
	update(script) {
		resolution(binding)
	}

fun Dictionary.plus(script: Script, body: Body): Dictionary =
	plus(script, binding(dictionary().function(body)))

fun Dictionary.update(script: Script, fn: Dictionary.() -> Resolution): Dictionary =
	null
		?: updateAnyOrNull(script, fn)
		?: updateExact(script, fn)

fun Dictionary.updateExact(script: Script, fn: Dictionary.() -> Resolution): Dictionary =
	when (script) {
		is UnitScript -> updateContinuation(token(emptyEnd), fn)
		is LinkScript -> update(script.link, fn)
	}

fun Dictionary.update(struct: ScriptLink, fn: Dictionary.() -> Resolution): Dictionary =
	update(struct.line) {
		resolution(
			update(struct.lhs, fn)
		)
	}

fun Dictionary.update(line: ScriptLine, fn: Dictionary.() -> Resolution): Dictionary =
	when (line) {
		is FieldScriptLine -> update(line.field, fn)
		is LiteralScriptLine -> update(line.literal, fn)
	}

fun Dictionary.update(literal: Literal, fn: Dictionary.() -> Resolution): Dictionary =
	updateContinuation(token(begin(literal.selectName))) {
		resolution(updateContinuation(token(literal.native)) {
			resolution(updateContinuation(token(emptyEnd), fn))
		})
	}

fun Dictionary.updateAnyOrNull(script: Script, fn: Dictionary.() -> Resolution): Dictionary? =
	notNullIf(script == script(anyName)) {
		updateAny(fn)
	}

fun Dictionary.update(field: ScriptField, fn: Dictionary.() -> Resolution): Dictionary =
	updateContinuation(token(begin(field.string))) {
		resolution(update(field.rhs, fn))
	}

fun Dictionary.updateAny(fn: Dictionary.() -> Resolution): Dictionary =
	removeForAny.updateContinuation(token(anyEnd), fn)

operator fun Dictionary.plus(dictionary: Dictionary): Dictionary =
	runIf(dictionary.resolutionOrNull(token(anyEnd)) != null) { removeForAny }
		.run {
			dictionary.tokenToResolutionMap.entries.fold(this) { dictionary, (token, resolution) ->
				dictionary.update(token) { resolutionOrNull ->
					resolutionOrNull.orNullMerge(resolution)
				}
			}
		}

fun Resolution.merge(resolution: Resolution): Resolution =
	when (resolution) {
		is BindingResolution -> resolution
		is DictionaryResolution ->
			when (this) {
				is BindingResolution -> resolution
				is DictionaryResolution -> resolution(dictionary.plus(resolution.dictionary))
			}
	}

fun Resolution?.orNullMerge(resolution: Resolution): Resolution =
	this?.merge(resolution) ?: resolution

fun Dictionary.switchOrNull(value: Value, script: Script): Value? =
	value.linkOrNull?.onlyLineOrNull?.fieldOrNull?.value?.let { switchBodyOrNull(it, script) }

fun Dictionary.switchBodyOrNull(value: Value, script: Script): Value? =
	value.linkOrNull?.onlyLineOrNull?.let {
		switchOrNull(it, script)
	}

fun Dictionary.switchOrNull(line: Line, script: Script): Value? =
	when (script) {
		is LinkScript -> switchOrNull(line, script.link)
		is UnitScript -> null
	}

fun Dictionary.switchOrNull(line: Line, scriptLink: ScriptLink): Value? =
	switchOrNull(line, scriptLink.line) ?: switchOrNull(line, scriptLink.lhs)

fun Dictionary.switchOrNull(line: Line, scriptLine: ScriptLine): Value? =
	when (scriptLine) {
		is FieldScriptLine -> switchOrNull(line, scriptLine.field)
		is LiteralScriptLine -> null
	}

fun Dictionary.switchOrNull(line: Line, scriptField: ScriptField): Value? =
	ifOrNull(line.selectName == scriptField.name) {
		line.selectValueOrNull?.let { given ->
			plusGiven(given).value(scriptField.rhs)
		}
	}

fun Dictionary.apply(block: Block, given: Value): Value =
	when (block.typeOrNull) {
		BlockType.REPEATEDLY -> applyRepeating(block.untypedScript, given)
		BlockType.RECURSIVELY -> applyRecursing(block.untypedScript, given)
		null -> applyUntyped(block.untypedScript, given)
	}

tailrec fun Dictionary.applyRepeating(script: Script, given: Value): Value {
	val result = plusGiven(given).value(script)
	val repeatValue = result.repeatValueOrNull
	return if (repeatValue != null) applyRepeating(script, repeatValue)
	else result
}

fun Dictionary.applyRecursing(script: Script, given: Value): Value =
	plusGiven(given).plusRecurse(script).value(script)

fun Dictionary.applyUntyped(script: Script, given: Value): Value =
	plusGiven(given).value(script)

fun Dictionary.plusRecurse(script: Script): Dictionary =
	plus(
		script(
			anyName lineTo script(),
			recurseName lineTo script()
		),
		binding(function(body(BlockType.RECURSIVELY.block(script))))
	)
