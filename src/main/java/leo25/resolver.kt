package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.*
import leo14.*
import leo14.matching.name

data class Resolver(val tokenToResolutionMap: PersistentMap<Token, Resolution>)

sealed class Token
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()
data class NativeToken(val native: Native) : Token()

data class Begin(val name: String)

sealed class End
object EmptyEnd : End()
object AnythingEnd : End()

sealed class Resolution
data class ResolverResolution(val resolver: Resolver) : Resolution()
data class BindingResolution(val binding: Binding) : Resolution()

fun Resolver.put(token: Token, resolution: Resolution): Resolver =
	Resolver(tokenToResolutionMap.put(token, resolution))

fun resolver(vararg pairs: Pair<Token, Resolution>): Resolver =
	Resolver(persistentMapOf()).fold(pairs) { put(it.first, it.second) }

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)
fun token(native: Native): Token = NativeToken(native)

fun begin(name: String) = Begin(name)
val emptyEnd: End = EmptyEnd
val anyEnd: End = AnythingEnd

fun resolution(resolver: Resolver): Resolution = ResolverResolution(resolver)
fun resolution(binding: Binding): Resolution = BindingResolution(binding)

fun Resolver.update(token: Token, fn: (Resolution?) -> Resolution?): Resolver =
	fn(tokenToResolutionMap[token]).let { resolutionOrNull ->
		Resolver(
			if (resolutionOrNull == null) tokenToResolutionMap.remove(token)
			else tokenToResolutionMap.put(token, resolutionOrNull)
		)
	}

fun Resolver.updateContinuation(token: Token, fn: Resolver.() -> Resolution): Resolver =
	update(token) { resolutionOrNull ->
		resolutionOrNull?.continuationResolver.orIfNull { resolver() }.fn()
	}

val Resolver.removeForAny: Resolver
	get() =
		Resolver(
			tokenToResolutionMap[token(anyEnd)].let { resolutionOrNull ->
				if (resolutionOrNull == null) persistentMapOf()
				else persistentMapOf(token(anyEnd) to resolutionOrNull)
			})

val Resolution.continuationResolver: Resolver
	get() =
		when (this) {
			is BindingResolution -> resolver()
			is ResolverResolution -> resolver
		}

fun Resolver.plus(definition: Definition): Resolver =
	update(definition.pattern.script) {
		resolution(definition.binding)
	}

fun Resolver.plus(script: Script, body: Body): Resolver =
	plus(definition(pattern(script), binding(resolver().function(body))))

fun Resolver.update(script: Script, fn: Resolver.() -> Resolution): Resolver =
	null
		?: updateAnyOrNull(script, fn)
		?: updateExact(script, fn)

fun Resolver.updateExact(script: Script, fn: Resolver.() -> Resolution): Resolver =
	when (script) {
		is UnitScript -> updateContinuation(token(emptyEnd), fn)
		is LinkScript -> update(script.link, fn)
	}

fun Resolver.update(link: ScriptLink, fn: Resolver.() -> Resolution): Resolver =
	update(link.line) {
		resolution(
			update(link.lhs, fn)
		)
	}

fun Resolver.update(line: ScriptLine, fn: Resolver.() -> Resolution): Resolver =
	when (line) {
		is FieldScriptLine -> update(line.field, fn)
		is LiteralScriptLine -> update(line.literal, fn)
	}

fun Resolver.update(literal: Literal, fn: Resolver.() -> Resolution): Resolver =
	updateContinuation(token(begin(literal.selectName))) {
		resolution(updateContinuation(token(literal.native), fn))
//		) {
//			resolution(updateContinuation(token(emptyEnd), fn))
//		})
	}

fun Resolver.updateAnyOrNull(script: Script, fn: Resolver.() -> Resolution): Resolver? =
	notNullIf(script == script(anyName)) {
		updateAny(fn)
	}

fun Resolver.update(field: ScriptField, fn: Resolver.() -> Resolution): Resolver =
	updateContinuation(token(begin(field.string))) {
		resolution(update(field.rhs, fn))
	}

fun Resolver.updateAny(fn: Resolver.() -> Resolution): Resolver =
	removeForAny.updateContinuation(token(anyEnd), fn)

operator fun Resolver.plus(resolver: Resolver): Resolver =
	runIf(resolver.resolutionOrNull(token(anyEnd)) != null) { removeForAny }
		.run {
			resolver.tokenToResolutionMap.entries.fold(this) { resolver, (token, resolution) ->
				resolver.update(token) { resolutionOrNull ->
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
				is ResolverResolution -> resolution(resolver.plus(resolution.resolver))
			}
	}

fun Resolution?.orNullMerge(resolution: Resolution): Resolution =
	this?.merge(resolution) ?: resolution

fun Resolver.switchOrNull(value: Value, script: Script): Value? =
	value.bodyOrNull?.let { switchBodyOrNull(it, script) }

fun Resolver.switchBodyOrNull(value: Value, script: Script): Value? =
	value.fieldOrNull?.let {
		switchOrNull(it, script)
	}

fun Resolver.switchOrNull(line: Field, script: Script): Value? =
	when (script) {
		is LinkScript -> switchOrNull(line, script.link)
		is UnitScript -> null
	}

fun Resolver.switchOrNull(line: Field, scriptLink: ScriptLink): Value? =
	switchOrNull(line, scriptLink.line) ?: switchOrNull(line, scriptLink.lhs)

fun Resolver.switchOrNull(line: Field, scriptLine: ScriptLine): Value? =
	when (scriptLine) {
		is FieldScriptLine -> switchOrNull(line, scriptLine.field)
		is LiteralScriptLine -> null
	}

fun Resolver.switchOrNull(line: Field, scriptField: ScriptField): Value? =
	ifOrNull(line.name == scriptField.name) {
		interpreter(value(line)).plus(scriptField.rhs).value
	}

fun Resolver.apply(body: Body, given: Value): Value =
	when (body) {
		is FnBody -> body.fn(given)
		is BlockBody -> apply(body.block, given)
	}

fun Resolver.apply(block: Block, given: Value): Value =
	when (block.typeOrNull) {
		BlockType.REPEATEDLY -> applyRepeating(block.untypedScript, given)
		BlockType.RECURSIVELY -> applyRecursing(block.untypedScript, given)
		null -> applyUntyped(block.untypedScript, given)
	}

tailrec fun Resolver.applyRepeating(script: Script, given: Value): Value {
	val result = set(given).value(script)
	val repeatValue = result.repeatValueOrNull
	return if (repeatValue != null) applyRepeating(script, repeatValue)
	else result
}

fun Resolver.applyRecursing(script: Script, given: Value): Value =
	set(given).plusRecurse(script).value(script)

fun Resolver.applyUntyped(script: Script, given: Value): Value =
	set(given).value(script)

fun Resolver.plusRecurse(script: Script): Resolver =
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

fun Resolver.plusOrNull(scriptField: ScriptField): Resolver? =
	when (scriptField.string) {
		"let" -> plusLetOrNull(scriptField.rhs)
		"set" -> plusSet(scriptField.rhs)
		else -> null
	}

fun Resolver.plusLetOrNull(rhs: Script): Resolver? =
	rhs.matchInfix(doName) { lhs, rhs ->
		plus(definition(pattern(lhs), binding(function(body(rhs)))))
	}

fun Resolver.plusSet(rhs: Script): Resolver =
	set(value(rhs))