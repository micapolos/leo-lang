package leo14.typed.compiler

import leo13.Stack
import leo13.push
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.Line
import leo14.typed.Typed
import leo14.typed.lineTo

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
						.updateCompiled { plusGiven(parentCompiledParser.compiled.typed.type) })
			}
		is EndToken ->
			parentCompiledParser.next { updateTyped { match.end() } }
	} ?: error("$this.parse($token)")

fun <T> MatchParser<T>.plus(name: String, typed: Typed<T>): MatchParser<T> =
	copy(
		match = Case(match, typed).end(),
		caseLineStack = caseLineStack.push(name lineTo typed.type))
