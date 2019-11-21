package leo14.typed.compiler

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.stack
import leo14.*
import leo14.typed.*

data class CompiledParser<T>(
	val parent: CompiledParserParent<T>?,
	val context: Context<T>,
	val phase: Phase,
	val compiled: Compiled<T>)

sealed class CompiledParserParent<T>
data class FieldCompiledParserParent<T>(val compiledParser: CompiledParser<T>, val name: String) : CompiledParserParent<T>()
data class ActionDoesParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class ActionDoParserParent<T>(val compiledParser: CompiledParser<T>, val action: Action<T>) : CompiledParserParent<T>()
data class GiveCompiledParserParent<T>(val compiledParser: CompiledParser<T>) : CompiledParserParent<T>()
data class RememberDoesParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class RememberIsParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class MatchParserParent<T>(val matchParser: MatchParser<T>, val name: String) : CompiledParserParent<T>()

fun <T> CompiledParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken ->
			CompiledParserLeo(resolve(context.compileLine(token.literal)))
		is BeginToken ->
			when (token.begin.string) {
				context.dictionary.action ->
					leo(TypeParser(null, ActionDoesTypeBeginner(this), context.dictionary, type()))
				context.dictionary.`as` ->
					leo(TypeParser(AsTypeParserParent(this), null, context.dictionary, type()))
				context.dictionary.`do` ->
					compiled.typed.action.let { action ->
						leo(CompiledParser(ActionDoParserParent(this, action), context, phase, compiled.begin))
					}
				context.dictionary.give ->
					leo(CompiledParser(GiveCompiledParserParent(this), context, phase, compiled.begin))
				context.dictionary.delete ->
					leo(DeleteParser(this))
				context.dictionary.nothing ->
					leo(NothingParser(this))
				context.dictionary.match ->
					leo(MatchParser(this, stack(), compiled.typed.beginMatch()))
				context.dictionary.make ->
					leo(ScriptParser(MakeScriptParserParent(this), script()))
				context.dictionary.remember ->
					leo(TypeParser(null, RememberTypeBeginner(this), context.dictionary, type()))
				context.dictionary.forget ->
					leo(TypeParser(ForgetTypeParserParent(this), null, context.dictionary, type()))
				else ->
					CompiledParserLeo(
						CompiledParser(
							FieldCompiledParserParent(this, token.begin.string),
							context,
							phase,
							compiled.begin))
			}
		is EndToken -> parent?.end(compiled.resolveForEnd)
	} ?: error("$this.parse($token)")

fun <T> CompiledParserParent<T>.end(compiled: Compiled<T>): Leo<T> =
	when (this) {
		is FieldCompiledParserParent ->
			leo(compiledParser.resolve(line(name fieldTo compiled.typed)))
		is ActionDoesParserParent ->
			leo(ActionParser(compiledParser, type does compiled.typed))
		is ActionDoParserParent ->
			leo(compiledParser.updateCompiled { updateTyped { action.`do`(compiled.typed) } })
		is GiveCompiledParserParent ->
			leo(compiledParser.updateCompiled { updateTyped { compiled.typed } })
		is RememberDoesParserParent ->
			leo(MemoryItemParser(compiledParser, remember(type does compiled.typed, needsInvoke = true)))
		is RememberIsParserParent ->
			leo(MemoryItemParser(compiledParser, remember(type does compiled.typed, needsInvoke = false)))
		is MatchParserParent ->
			leo(matchParser.plus(name, compiled))
	}

fun <T> CompiledParser<T>.resolve(line: TypedLine<T>) =
	copy(compiled = compiled.resolve(line, context).resolve(phase, context.nativeApply))

fun <T> CompiledParser<T>.updateCompiled(fn: Compiled<T>.() -> Compiled<T>) =
	copy(compiled = compiled.fn())

val <T> CompiledParser<T>.delete
	get() =
		updateCompiled { updateTyped { typed() } }

fun <T> CompiledParser<T>.make(script: Script): CompiledParser<T> =
	when (script) {
		is UnitScript -> null
		is LinkScript -> ifOrNull(script.link.lhs.isEmpty) {
			when (script.link.line) {
				is LiteralScriptLine -> null
				is FieldScriptLine ->
					notNullIf(script.link.line.field.rhs.isEmpty) {
						updateCompiled { updateTyped { resolveWrap(script.link.line.field.string) } }
					}
			}
		}
	} ?: error("$this.make($script)")