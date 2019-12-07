package leo14.typed.compiler

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.index0
import leo13.stack
import leo14.*
import leo14.typed.*

data class CompiledParser<T>(
	val parent: CompiledParserParent<T>?,
	val kind: CompilerKind,
	val context: Context<T>,
	val compiled: Compiled<T>)

fun <T> CompiledParser<T>.compile(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken -> compile(token.literal)
		is BeginToken ->
			token.begin.string.let {
				when (it keywordOrNullIn context.language) {
					Keyword.AS -> compileAs
					Keyword.DEFINE -> compileDefine
					Keyword.GIVE -> compileGive
					Keyword.FUNCTION -> compileFunction
					Keyword.MAKE -> compileMake
					Keyword.MATCH -> compileMatch
					Keyword.QUOTE -> compileQuote
					else -> compile(it)
				}
			}
		is EndToken -> end
	} ?: error("$this.compile($token)")


fun <T> CompiledParser<T>.nextCompiler(fn: Compiled<T>.() -> Compiled<T>): Compiler<T> =
	compiler(next(fn))

fun <T> CompiledParser<T>.next(fn: Compiled<T>.() -> Compiled<T>): CompiledParser<T> =
	updateCompiled(fn).next

val <T> CompiledParser<T>.next: CompiledParser<T>
	get() =
		when (kind) {
			CompilerKind.COMPILER -> this
			CompilerKind.EVALUATOR -> evaluate
		}

val <T> CompiledParser<T>.evaluate: CompiledParser<T>
	get() =
		updateCompiled { eval(context.evaluator) }

fun <T> CompiledParser<T>.resolveCompiler(line: TypedLine<T>): Compiler<T> =
	compile(line) ?: compiler(copy(compiled = compiled.resolve(line, context)).next)

fun <T> CompiledParser<T>.compile(line: TypedLine<T>): Compiler<T>? =
	parse(line)?.let { compiler(it) }

fun <T> CompiledParser<T>.parse(typed: TypedLine<T>): CompiledParser<T>? =
	when (typed.line) {
		is NativeLine -> null
		is FieldLine -> parse(typed.term of typed.line.field)
		is ChoiceLine -> null
		is ArrowLine -> null
		AnyLine -> null
	}

fun <T> CompiledParser<T>.parse(typed: TypedField<T>): CompiledParser<T>? =
	typed.field.string
		.keywordOrNullIn(context.language)
		?.let { parse(it, typed.rhs) }

fun <T> CompiledParser<T>.parse(keyword: Keyword, rhs: Typed<T>): CompiledParser<T>? =
	when (keyword) {
		Keyword.APPLY -> parseApply(rhs)
		Keyword.DELETE -> parseDelete(rhs)
		Keyword.EVALUATE -> parseEvaluate(rhs)
		Keyword.GIVE -> parseGive(rhs)
		else -> null
	}

fun <T> CompiledParser<T>.parseApply(rhs: Typed<T>): CompiledParser<T>? =
	compiled.typed.functionOrNull?.applyOrNull(rhs)?.let { applied ->
		next { updateTyped { applied } }
	}

fun <T> CompiledParser<T>.parseDelete(rhs: Typed<T>): CompiledParser<T>? =
	notNullIf(rhs.type == type()) {
		parseDelete
	}

fun <T> CompiledParser<T>.parseEvaluate(rhs: Typed<T>): CompiledParser<T>? =
	ifOrNull(kind == CompilerKind.EVALUATOR && rhs.type == type()) {
		parseEvaluate
	}

fun <T> CompiledParser<T>.parseGive(rhs: Typed<T>): CompiledParser<T>? =
	next { updateTyped { rhs } }

val <T> CompiledParser<T>.parseDelete
	get() =
		next { updateTyped { typed() } }

val <T> CompiledParser<T>.parseEvaluate: CompiledParser<T>?
	get() =
		(compiler(this.updateCompiled { updateTyped { typed() } }).parse(decompile) as? CompiledParserCompiler)?.compiledParser

fun <T> CompiledParser<T>.updateCompiled(fn: Compiled<T>.() -> Compiled<T>) =
	copy(compiled = compiled.fn())

fun <T> CompiledParser<T>.make(script: Script): Compiler<T> =
	when (script) {
		is UnitScript -> null
		is LinkScript -> ifOrNull(script.link.lhs.isEmpty) {
			when (script.link.line) {
				is LiteralScriptLine -> null
				is FieldScriptLine ->
					notNullIf(script.link.line.field.rhs.isEmpty) {
						nextCompiler { updateTyped { resolveWrap(script.link.line.field.string) } }
					}
			}
		}
	} ?: error("$this.make($script)")

fun <T> CompiledParser<T>.compile(literal: Literal) =
	resolveCompiler(context.compileLine(literal))

fun <T> CompiledParser<T>.plus(script: Script): CompiledParser<T> =
	updateCompiled { updateTyped { plus(script, context.literalCompile) } }

val <T> CompiledParser<T>.decompile: Script
	get() =
		compiled.typed.decompile(context.decompileLiteral, context.termDecompile)

val <T> CompiledParser<T>.beginGive
	get(): CompiledParser<T> =
		begin(FieldCompiledParserParent(this, Keyword.GIVE stringIn context.language))
			.updateCompiled { plusGiven(Keyword.GIVEN stringIn context.language, compiled.typed) }

val <T> CompiledParser<T>.compileFunction
	get() =
		compiler(TypeParser(null, FunctionGivesTypeBeginner(this), context.language, context.typeContext, type()))

val <T> CompiledParser<T>.compileAs
	get() =
		compiler(TypeParser(AsTypeParserParent(this), null, context.language, context.typeContext, type()))

val <T> CompiledParser<T>.compileCompiler
	get() =
		compiler(QuoteParser(CompiledQuoteParserParent(this), script()))

val <T> CompiledParser<T>.compileEvaluator
	get() =
		compiler(QuoteParser(CompiledQuoteParserParent(this), script()))

val <T> CompiledParser<T>.compileMatch
	get() =
		compiler(MatchParser(this, stack(), compiled.typed.beginMatch()))

val <T> CompiledParser<T>.compileMake
	get() =
		compiler(QuoteParser(MakeQuoteParserParent(this), script()))

val <T> CompiledParser<T>.compileDefine
	get() =
		compiler(DefineParser(this, compiled.memory, index0))

val <T> CompiledParser<T>.compileGive
	get() =
		compiler(beginGive)

val <T> CompiledParser<T>.compileQuote
	get() =
		compiler(QuoteParser(CompiledQuoteParserParent(this), script()))

fun <T> CompiledParser<T>.compile(string: String) =
	CompiledParserCompiler(
		begin(FieldCompiledParserParent(this, string)))

val <T> CompiledParser<T>.end
	get() =
		parent?.end(compiled.typedForEnd)

fun <T> CompiledParser<T>.begin(parent: CompiledParserParent<T>): CompiledParser<T> =
	begin(parent, kind)

fun <T> CompiledParser<T>.begin(parent: CompiledParserParent<T>, kind: CompilerKind): CompiledParser<T> =
	CompiledParser(
		parent,
		kind,
		context,
		compiled.begin)

fun <T> CompiledParser<T>.compile(field: Field): Compiler<T> =
	resolveCompiler(field.scriptLine.staticTypedLine(context.literalCompile))
