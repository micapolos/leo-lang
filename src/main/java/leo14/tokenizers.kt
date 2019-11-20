package leo14

import leo13.Stack
import leo14.syntax.Syntax
import leo14.syntax.valueSyntax

fun Processor<Token>.process(script: Script): Processor<Token> =
	when (script) {
		is UnitScript -> this
		is LinkScript -> process(script.link)
	}

fun Processor<Token>.process(link: ScriptLink) =
	process(link.lhs).process(link.line)

fun Processor<Token>.process(line: ScriptLine) =
	when (line) {
		is LiteralScriptLine -> process(token(line.literal))
		is FieldScriptLine -> process(line.field)
	}

fun Processor<Token>.process(field: ScriptField) =
	this
		.process(token(begin(field.string)))
		.process(field.rhs)
		.process(token(end))

val Script.tokenStack: Stack<Token>
	get() =
		let { processStack { process(it) } }

fun Processor<Syntax>.syntaxProcess(script: Script): Processor<Syntax> =
	map(Token::valueSyntax).process(script).map(Syntax::token)

