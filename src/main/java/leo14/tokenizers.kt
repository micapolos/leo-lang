package leo14

import leo.base.Seq
import leo13.Stack
import leo13.reverse
import leo13.seq
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
		is LiteralScriptLine -> processFn(token(line.literal))
		is FieldScriptLine -> process(line.field)
	}

fun Processor<Token>.process(field: ScriptField) =
	this
		.processFn(token(begin(field.string)))
		.process(field.rhs)
		.processFn(token(end))

val Script.tokenStack: Stack<Token>
	get() =
		let { processStack { process(it) } }

val Script.tokenSeq: Seq<Token>
	get() =
		tokenStack.reverse.seq

fun Processor<Syntax>.syntaxProcess(script: Script): Processor<Syntax> =
	map(Token::valueSyntax).process(script).map(Syntax::token)
