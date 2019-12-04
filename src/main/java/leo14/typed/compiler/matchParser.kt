package leo14.typed.compiler

import leo13.Stack
import leo13.push
import leo14.*
import leo14.typed.Line
import leo14.typed.Typed
import leo14.typed.lineTo
import leo14.typed.scriptLine

data class MatchParser<T>(
	val parentCompiledParser: CompiledParser<T>,
	val caseLineStack: Stack<Line>,
	val match: Match<T>)

fun <T> MatchParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken ->
			null
		is BeginToken -> match.beginCase(token.begin.string)
			.let { case ->
				compiler(
					parentCompiledParser
						.begin(MatchParserParent(copy(match = case.match), token.begin.string), CompilerKind.COMPILER)
						.updateCompiled {
							plusGiven(
								Keyword.GIVEN stringIn parentCompiledParser.context.language,
								case.typed.type)
						})
			}
		is EndToken ->
			parentCompiledParser.nextCompiler { updateTyped { match.end() } }
	} ?: error("$this.parse($token)")

fun <T> MatchParser<T>.plus(name: String, typed: Typed<T>): MatchParser<T> =
	copy(
		match = Case(match, typed).end(),
		caseLineStack = caseLineStack.push(name lineTo typed.type))

val <T> MatchParser<T>.reflectScriptLine
	get() =
		"match" lineTo script(
			"parser" lineTo script(
				"parent" lineTo script(parentCompiledParser.reflectScriptLine),
				caseLineStack.reflectOrEmptyScriptLine("cases") { scriptLine },
				match.reflectScriptLine)
		)