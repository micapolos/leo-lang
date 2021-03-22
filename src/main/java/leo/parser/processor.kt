package leo.parser

import leo.base.notNullIf
import leo.base.orNull
import leo.base.println
import leo13.Stack
import leo13.fold
import leo13.push
import leo13.stack

data class Processor<out State, in Input>(
	val state: State,
	val process: (Input) -> Processor<State, Input>?
)

val <Input> Stack<Input>.pushProcessor: Processor<Stack<Input>, Input>
	get() =
		Processor(this) { input ->
			push(input).pushProcessor
		}

data class Parser<in Input, out Parsed>(
	val parse: (Input) -> Parse<Input, Parsed>?
)

sealed class Parse<in Input, out Parsed>
data class ParserParse<Input, Parsed>(val parser: Parser<Input, Parsed>) : Parse<Input, Parsed>()
data class ParsedParse<Input, Parsed>(val parsed: Parsed) : Parse<Input, Parsed>()

fun <Input, Parsed> errorParser(): Parser<Input, Parsed> = Parser { null }

object Tab

val emptyTabParser: Parser<Char, Tab>
	get() =
		Parser { char ->
			notNullIf(char == ' ') {
				ParserParse(singleSpaceTabParser)
			}
		}

val singleSpaceTabParser: Parser<Char, Tab>
	get() =
		Parser { char ->
			notNullIf(char == ' ') {
				ParsedParse<Char, Tab>(Tab)
			}
		}

object Begin
object End

sealed class IndentedToken
data class BeginIndentedToken(val begin: Begin) : IndentedToken()
data class EndIndentedToken(val end: End) : IndentedToken()
data class CharIndentedToken(val char: Char) : IndentedToken()

data class Indent(val tabStack: Stack<Tab>)

data class Lead(
	val leftIndent: Indent,
	val rightIndent: Indent,
	val tabParser: Parser<Char, Tab>
)

data class Body(
	val indent: Indent
)

sealed class IndentState
data class LeadIndentState(val lead: Lead) : IndentState()
data class BodyIndentState(val body: Body) : IndentState()

fun <State> Processor<State, IndentedToken>.emptyLead() =
	lead(Indent(stack()), Indent(stack()), ParsedParse(Tab))

fun <State> Processor<State, IndentedToken>.lead(
	leftIndent: Indent,
	rightIndent: Indent,
	tabParse: Parse<Char, Tab>
): Processor<State, Char> = Processor(state) { char ->
	when (tabParse) {
		is ParserParse -> tabParse.parser.parse(char)?.let { parse ->
			lead(leftIndent, rightIndent, parse)
		}
		is ParsedParse ->
			emptyTabParser.parse(char).let { parse ->
				if (parse != null) lead(
					leftIndent,
					Indent(rightIndent.tabStack.push(tabParse.parsed)),
					parse)
				else orNull
					.fold(rightIndent.tabStack) { process(EndIndentedToken(End)) }
					?.process?.invoke(CharIndentedToken(char))
					?.body(leftIndent)
			}
	}
}

fun <State> Processor<State, IndentedToken>.body(
	indent: Indent
): Processor<State, Char> = Processor(state) { char ->
	if (char == '\n')
		lead(
			leftIndent = Indent(stack()),
			rightIndent = Indent(indent.tabStack.push(Tab)),
			tabParse = ParsedParse(Tab))
	else
		process(CharIndentedToken(char)).body(indent)
}

fun main() {
	stack<IndentedToken>()
		.pushProcessor
		.emptyLead()
		?.process.invoke('a')
		?.process?.invoke('\n')
		?.process?.invoke(' ')
		?.process?.invoke(' ')
		?.process?.invoke('b')
		?.state
		?.println
}