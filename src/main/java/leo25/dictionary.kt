package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
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
data class ResolverResolution(val dictionary: Dictionary) : Resolution()
data class BindingResolution(val binding: Binding) : Resolution()

fun Dictionary.put(token: Token, resolution: Resolution): Dictionary =
	Dictionary(tokenToResolutionMap.put(token, resolution))

fun dictionary(vararg pairs: Pair<Token, Resolution>): Dictionary =
	Dictionary(persistentHashMapOf()).fold(pairs) { put(it.first, it.second) }

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)
fun token(native: Native): Token = NativeToken(native)

fun begin(name: String) = Begin(name)
val emptyEnd: End = EmptyEnd
val anyEnd: End = AnythingEnd

fun resolution(dictionary: Dictionary): Resolution = ResolverResolution(dictionary)
fun resolution(binding: Binding): Resolution = BindingResolution(binding)

inline fun Dictionary.update(token: Token, fn: (Resolution?) -> Resolution?): Dictionary =
	fn(tokenToResolutionMap[token]).let { resolutionOrNull ->
		Dictionary(
			if (resolutionOrNull == null) tokenToResolutionMap.remove(token)
			else tokenToResolutionMap.put(token, resolutionOrNull)
		)
	}

inline fun Dictionary.updateContinuation(token: Token, fn: Dictionary.() -> Resolution): Dictionary =
	update(token) { resolutionOrNull ->
		resolutionOrNull?.continuationDictionary.orIfNull { dictionary() }.fn()
	}

val Dictionary.removeForAny: Dictionary
	get() =
		Dictionary(
			tokenToResolutionMap[token(anyEnd)].let { resolutionOrNull ->
				if (resolutionOrNull == null) persistentHashMapOf()
				else persistentHashMapOf(token(anyEnd) to resolutionOrNull)
			})

val Resolution.continuationDictionary: Dictionary
	get() =
		when (this) {
			is BindingResolution -> dictionary()
			is ResolverResolution -> dictionary
		}

fun Dictionary.plus(definition: Definition): Dictionary =
	update(definition.pattern.script) {
		resolution(definition.binding)
	}

fun Dictionary.plus(script: Script, body: Body): Dictionary =
	plus(definition(pattern(script), binding(dictionary().function(body))))

fun Dictionary.update(script: Script, fn: Dictionary.() -> Resolution): Dictionary =
	null
		?: updateAnyOrNull(script, fn)
		?: updateExact(script, fn)

inline fun Dictionary.updateExact(script: Script, noinline fn: Dictionary.() -> Resolution): Dictionary =
	when (script) {
		is UnitScript -> updateContinuation(token(emptyEnd), fn)
		is LinkScript -> update(script.link, fn)
	}

inline fun Dictionary.update(link: ScriptLink, noinline fn: Dictionary.() -> Resolution): Dictionary =
	update(link.line) {
		resolution(
			update(link.lhs, fn)
		)
	}

inline fun Dictionary.update(line: ScriptLine, noinline fn: Dictionary.() -> Resolution): Dictionary =
	when (line) {
		is FieldScriptLine -> update(line.field, fn)
		is LiteralScriptLine -> update(line.literal, fn)
	}

inline fun Dictionary.update(literal: Literal, fn: Dictionary.() -> Resolution): Dictionary =
	updateContinuation(token(begin(literal.selectName))) {
		resolution(updateContinuation(token(literal.native), fn))
	}

inline fun Dictionary.updateAnyOrNull(script: Script, fn: Dictionary.() -> Resolution): Dictionary? =
	notNullIf(script == script(anyName)) {
		updateAny(fn)
	}

inline fun Dictionary.update(field: ScriptField, noinline fn: Dictionary.() -> Resolution): Dictionary =
	updateContinuation(token(begin(field.string))) {
		resolution(update(field.rhs, fn))
	}

inline fun Dictionary.updateAny(fn: Dictionary.() -> Resolution): Dictionary =
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
		is ResolverResolution ->
			when (this) {
				is BindingResolution -> resolution
				is ResolverResolution -> resolution(dictionary.plus(resolution.dictionary))
			}
	}

fun Resolution?.orNullMerge(resolution: Resolution): Resolution =
	this?.merge(resolution) ?: resolution

fun Dictionary.switchOrNullLeo(value: Value, script: Script): Leo<Value?> =
	value.bodyOrNull?.let { switchBodyOrNull(it, script) } ?: leo(null)

fun Dictionary.switchBodyOrNull(value: Value, script: Script): Leo<Value?> =
	value.fieldOrNull?.let {
		switchOrNullLeo(it, script)
	} ?: leo(null)

fun Dictionary.switchOrNullLeo(line: Field, script: Script): Leo<Value?> =
	when (script) {
		is LinkScript -> switchOrNullLeo(line, script.link)
		is UnitScript -> leo(null)
	}

fun Dictionary.switchOrNullLeo(line: Field, scriptLink: ScriptLink): Leo<Value?> =
	switchOrNullLeo(line, scriptLink.line).or {
		switchOrNullLeo(line, scriptLink.lhs)
	}

fun Dictionary.switchOrNullLeo(line: Field, scriptLine: ScriptLine): Leo<Value?> =
	when (scriptLine) {
		is FieldScriptLine -> switchOrNullLeo(line, scriptLine.field)
		is LiteralScriptLine -> leo(null)
	}

fun Dictionary.switchOrNullLeo(line: Field, scriptField: ScriptField): Leo<Value?> =
	ifOrNull(line.name == scriptField.name) {
		context.interpreter(value(line)).plusLeo(scriptField.rhs).map { it.value }
	} ?: leo(null)

fun Dictionary.applyLeo(body: Body, given: Value): Leo<Value> =
	when (body) {
		is FnBody -> body.fn(set(given)).leo
		is BlockBody -> applyLeo(body.block, given)
	}

fun Dictionary.applyLeo(block: Block, given: Value): Leo<Value> =
	when (block.typeOrNull) {
		BlockType.REPEATEDLY -> applyRepeatingLeo(block.untypedScript, given)
		BlockType.RECURSIVELY -> applyRecursingLeo(block.untypedScript, given)
		null -> applyUntypedLeo(block.untypedScript, given)
	}

fun Dictionary.applyRepeatingLeo(script: Script, given: Value): Leo<Value> =
	given.leo.bindRepeating { given ->
		set(given).valueLeo(script)
	}

fun Dictionary.applyRecursingLeo(script: Script, given: Value): Leo<Value> =
	set(given).plusRecurse(script).valueLeo(script)

fun Dictionary.applyUntypedLeo(script: Script, given: Value): Leo<Value> =
	set(given).valueLeo(script)

fun Dictionary.plusRecurse(script: Script): Dictionary =
	plus(
		definition(
			pattern(
				script(
					anyName lineTo script(),
					recurseName lineTo script()
				)
			),
			binding(function(body(BlockType.RECURSIVELY.block(script))))
		)
	)

fun Dictionary.definitionSeqOrNullLeo(scriptField: ScriptField): Leo<Seq<Definition>?> =
	when (scriptField.string) {
		"let" -> letDefinitionOrNull(scriptField.rhs).nullableMap { seq(it) }
		"set" -> setDefinitionSeqLeo(scriptField.rhs)
		else -> leo(null)
	}

fun Dictionary.letDefinitionOrNull(rhs: Script): Leo<Definition?> =
	null
		?: letDoDefinitionOrNull(rhs)?.leo
		?: letBeDefinitionOrNull(rhs)

fun Dictionary.letDoDefinitionOrNull(rhs: Script): Definition? =
	rhs.matchInfix(doName) { lhs, rhs ->
		definition(pattern(lhs), binding(function(body(rhs))))
	}

fun Dictionary.letBeDefinitionOrNull(rhs: Script): Leo<Definition?> =
	rhs.matchInfix(beName) { lhs, rhs ->
		valueLeo(rhs).bind { value ->
			definition(pattern(lhs), binding(value)).leo
		}
	} ?: leo(null)

fun Dictionary.setDefinitionSeqLeo(rhs: Script): Leo<Seq<Definition>> =
	valueLeo(rhs).map { value ->
		value.setDefinitionSeq
	}