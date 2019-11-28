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
data class UseCompiledParserParent<T>(val compiledParser: CompiledParser<T>) : CompiledParserParent<T>()
data class RememberDoesParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class RememberIsParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class MatchParserParent<T>(val matchParser: MatchParser<T>, val name: String) : CompiledParserParent<T>()

fun <T> CompiledParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken ->
			compiler(plus(token.literal))
		is BeginToken ->
			when (token.begin.string) {
				context.dictionary.action ->
					compiler(TypeParser(null, ActionDoesTypeBeginner(this), context.dictionary, context.typeContext, type()))
				context.dictionary.`as` ->
					compiler(TypeParser(AsTypeParserParent(this), null, context.dictionary, context.typeContext, type()))
				context.dictionary.`do` ->
					compiled.typed.action.let { action ->
						compiler(CompiledParser(ActionDoParserParent(this, action), context, phase, compiled.begin))
					}
				context.dictionary.give ->
					compiler(CompiledParser(GiveCompiledParserParent(this), context, phase, compiled.begin))
				context.dictionary.delete ->
					compiler(DeleteParser(this))
				context.dictionary.nothing ->
					compiler(NothingParser(this))
				context.dictionary.match ->
					compiler(MatchParser(this, stack(), compiled.typed.beginMatch()))
				context.dictionary.make ->
					compiler(ScriptParser(MakeScriptParserParent(this), script()))
				context.dictionary.remember ->
					compiler(TypeParser(null, RememberTypeBeginner(this), context.dictionary, context.typeContext, type()))
				context.dictionary.forget ->
					compiler(TypeParser(ForgetTypeParserParent(this), ForgetTypeBeginner(this), context.dictionary, context.typeContext, type()))
				context.dictionary.script ->
					notNullIf(phase == Phase.EVALUATOR) {
						compiler(ScriptParser(CompiledScriptParserParent(this), script()))
					}
				context.dictionary.leonardo ->
					compiler(LeonardoParser(this))
				context.dictionary.use ->
					compiler(use)
				else ->
					CompiledParserCompiler(
						CompiledParser(
							FieldCompiledParserParent(this, token.begin.string),
							context,
							phase,
							compiled.begin))
			}
		is EndToken -> parent?.end(compiled.resolveForEnd)
	} ?: error("$this.parse($token)")

fun <T> CompiledParserParent<T>.end(compiled: Compiled<T>): Compiler<T> =
	when (this) {
		is FieldCompiledParserParent ->
			compiler(compiledParser.resolve(line(name fieldTo compiled.typed)))
		is ActionDoesParserParent ->
			compiler(ActionParser(compiledParser, type does compiled.typed))
		is ActionDoParserParent ->
			compiledParser.next { updateTyped { action.`do`(compiled.typed) } }
		is GiveCompiledParserParent ->
			compiledParser.next { updateTyped { compiled.typed } }
		is UseCompiledParserParent ->
			compiledParser.next { updateTyped { compiled.typed } }
		is RememberDoesParserParent ->
			compiler(MemoryItemParser(compiledParser, remember(type does compiled.typed, needsInvoke = true)))
		is RememberIsParserParent ->
			compiler(MemoryItemParser(compiledParser, remember(type does compiled.typed, needsInvoke = false)))
		is MatchParserParent ->
			compiler(matchParser.plus(name, compiled))
	}

fun <T> CompiledParser<T>.next(fn: Compiled<T>.() -> Compiled<T>): Compiler<T> =
	compiler(updateCompiled(fn).resolvePhase)

val <T> CompiledParser<T>.resolvePhase: CompiledParser<T>
	get() =
		updateCompiled { resolve(phase, context.evaluator) }

fun <T> CompiledParser<T>.resolve(line: TypedLine<T>) =
	copy(compiled = compiled.resolve(line, context).resolve(phase, context.evaluator))

fun <T> CompiledParser<T>.updateCompiled(fn: Compiled<T>.() -> Compiled<T>) =
	copy(compiled = compiled.fn())

val <T> CompiledParser<T>.delete
	get() =
		next { updateTyped { typed() } }

fun <T> CompiledParser<T>.make(script: Script): Compiler<T> =
	when (script) {
		is UnitScript -> null
		is LinkScript -> ifOrNull(script.link.lhs.isEmpty) {
			when (script.link.line) {
				is LiteralScriptLine -> null
				is FieldScriptLine ->
					notNullIf(script.link.line.field.rhs.isEmpty) {
						next { updateTyped { resolveWrap(script.link.line.field.string) } }
					}
			}
		}
	} ?: error("$this.make($script)")

fun <T> CompiledParser<T>.plus(literal: Literal) =
	resolve(context.compileLine(literal))

fun <T> CompiledParser<T>.plus(script: Script): CompiledParser<T> =
	updateCompiled { updateTyped { plus(script, context.literalCompile) } }

val <T> CompiledParser<T>.forgetEverything: CompiledParser<T>
	get() =
		updateCompiled { updateMemory { memory() } }

val <T> CompiledParser<T>.decompile: Script
	get() =
		compiled.typed.decompile(context.decompileLiteral)

val <T> CompiledParser<T>.use
	get(): CompiledParser<T> =
		CompiledParser(
			UseCompiledParserParent(this),
			context,
			phase,
			compiled.use)
