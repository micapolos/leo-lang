package leo14.typed.compiler

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.stack
import leo14.*
import leo14.typed.*
import leo14.typed.Function

data class CompiledParser<T>(
	val parent: CompiledParserParent<T>?,
	val context: Context<T>,
	val phase: Phase,
	val compiled: Compiled<T>)

sealed class CompiledParserParent<T>
data class FieldCompiledParserParent<T>(val compiledParser: CompiledParser<T>, val name: String) : CompiledParserParent<T>()
data class FunctionDoesParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class FunctionGiveParserParent<T>(val compiledParser: CompiledParser<T>, val function: Function<T>) : CompiledParserParent<T>()
data class GiveCompiledParserParent<T>(val compiledParser: CompiledParser<T>) : CompiledParserParent<T>()
data class UseCompiledParserParent<T>(val compiledParser: CompiledParser<T>) : CompiledParserParent<T>()
data class RememberDoesParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class RememberIsParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class MatchParserParent<T>(val matchParser: MatchParser<T>, val name: String) : CompiledParserParent<T>()

fun <T> CompiledParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken -> parse(token.literal)
		is BeginToken ->
			token.begin.string.let {
				when (it keywordOrNullIn context.language) {
					Keyword.FUNCTION -> beginFunction
					Keyword.AS -> beginAs
					Keyword.DO -> beginDo
					Keyword.GIVE -> beginGive
					Keyword.DELETE -> beginDelete
					Keyword.NOTHING -> beginNothing
					Keyword.MATCH -> beginMatch
					Keyword.MAKE -> beginMake
					Keyword.REMEMBER -> beginRemember
					Keyword.FORGET -> beginForget
					Keyword.SCRIPT -> beginScript
					Keyword.LEONARDO -> beginLeonardo
					Keyword.USE -> beginUse
					else -> begin(it)
				}
			}
		is EndToken -> end
	} ?: error("$this.parse($token)")

fun <T> CompiledParserParent<T>.end(compiled: Compiled<T>): Compiler<T> =
	when (this) {
		is FieldCompiledParserParent ->
			compiler(compiledParser.resolve(line(name fieldTo compiled.typed)))
		is FunctionDoesParserParent ->
			compiler(FunctionParser(compiledParser, type does compiled.typed))
		is FunctionGiveParserParent ->
			compiledParser.next { updateTyped { function.give(compiled.typed) } }
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
		when (phase) {
			Phase.COMPILER -> this
			Phase.EVALUATOR -> evaluate
		}

val <T> CompiledParser<T>.evaluate
	get() =
		updateCompiled { eval(context.evaluator) }

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

fun <T> CompiledParser<T>.parse(literal: Literal) =
	compiler(resolve(context.compileLine(literal)))

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

val <T> CompiledParser<T>.beginFunction
	get() =
		compiler(TypeParser(null, FunctionGivesTypeBeginner(this), context.language, context.typeContext, type()))

val <T> CompiledParser<T>.beginAs
	get() =
		compiler(TypeParser(AsTypeParserParent(this), null, context.language, context.typeContext, type()))

val <T> CompiledParser<T>.beginDo
	get() =
		compiled.typed.function.let { action ->
			compiler(CompiledParser(FunctionGiveParserParent(this, action), context, phase, compiled.begin))
		}

val <T> CompiledParser<T>.beginGive
	get() =
		compiler(CompiledParser(GiveCompiledParserParent(this), context, phase, compiled.begin))

val <T> CompiledParser<T>.beginDelete
	get() =
		compiler(DeleteParser(this))

val <T> CompiledParser<T>.beginNothing
	get() =
		compiler(NothingParser(this))

val <T> CompiledParser<T>.beginMatch
	get() =
		compiler(MatchParser(this, stack(), compiled.typed.beginMatch()))

val <T> CompiledParser<T>.beginMake
	get() =
		compiler(ScriptParser(MakeScriptParserParent(this), script()))

val <T> CompiledParser<T>.beginRemember
	get() =
		compiler(TypeParser(null, RememberTypeBeginner(this), context.language, context.typeContext, type()))

val <T> CompiledParser<T>.beginForget
	get() =
		compiler(TypeParser(ForgetTypeParserParent(this), ForgetTypeBeginner(this), context.language, context.typeContext, type()))

val <T> CompiledParser<T>.beginScript
	get() =
		notNullIf(phase == Phase.EVALUATOR) {
			compiler(ScriptParser(CompiledScriptParserParent(this), script()))
		}

val <T> CompiledParser<T>.beginLeonardo
	get() =
		compiler(LeonardoParser(this))

val <T> CompiledParser<T>.beginUse
	get() =
		compiler(use)

fun <T> CompiledParser<T>.begin(string: String) =
	CompiledParserCompiler(
		CompiledParser(
			FieldCompiledParserParent(this, string),
			context,
			phase,
			compiled.begin))

val <T> CompiledParser<T>.end
	get() =
		parent?.end(compiled.resolveForEnd)