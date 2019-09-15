package leo13.token.reader

import leo.base.fold
import leo.base.orNullIf
import leo.base.updateIfNotNull
import leo13.*
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.Token
import leo13.token.closing
import leo13.token.opening
import leo13.token.token

data class Tokenizer(
	val tokenProcessor: Processor<Token>,
	val parent: Parent,
	val head: Head) : ObjectScripting(), Processor<Char> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = "tokenizer" lineTo script(
			tokenProcessor.scriptingLine,
			parent.scriptableLine,
			head.scriptableLine)

	override fun process(char: Char) = push(char)
}

fun Processor<Token>.tokenizer(parent: Parent = parent(), head: Head = head()): Tokenizer =
	Tokenizer(this, parent, head)

fun tokenizer(vararg tokens: Token) =
	processor(*tokens).tokenizer()

fun Tokenizer.push(string: String): Tokenizer =
	fold(string) { push(it) }

fun Tokenizer.push(char: Char): Tokenizer =
	trace {
		"tokenizer" lineTo script(char.scriptLine)
	}.traced {
		pushOrNull(char).orTracedError
	}

fun Tokenizer.pushOrNull(char: Char): Tokenizer? =
	when (char) {
		' ' -> pushSpaceOrNull
		'\t' -> pushTabOrNull
		':' -> pushColonOrNull
		'\n' -> pushNewlineOrNull
		else -> pushOtherOrNull(char)
	}

val Tokenizer.pushSpaceOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead ->
				if (head.input.name.isEmpty()) null
				else tokenProcessor
					.process(token(opening(head.input.name)))
					.process(token(closing))
					.tokenizer(parent, head(input(head.input.colon, "")))
			is ColonHead ->
				tokenProcessor.tokenizer(parent, head(input(colon(true), "")))
			is IndentHead -> null
		}

val Tokenizer.pushTabOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead -> null
			is ColonHead -> null
			is IndentHead ->
				tokenProcessor.tokenizer(
					parent.plus(head.indent.tab),
					head.indent.previousOrNull
						?.let { previous -> head(previous) }
						?: head(input(colon(false), "")))
		}

val Tokenizer.pushColonOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead ->
				if (head.input.name.isEmpty()) null
				else tokenProcessor
					.process(token(opening(head.input.name)))
					.tokenizer(
						if (head.input.colon.boolean) parent.plus(space)
						else parent.plus(tab(space)),
						head(colon))
			is ColonHead -> null
			is IndentHead -> null
		}

val Tokenizer.pushNewlineOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead ->
				if (head.input.name.isEmpty())
					orNullIf(parent.indentOrNull != null)
				else tokenProcessor
					.process(token(opening(head.input.name)))
					.tokenizer(
						parent(),
						if (head.input.colon.boolean) head(parent.indentOrNull.orNullPlus(space).reverse)
						else head(parent.indentOrNull.orNullPlus(tab(space)).reverse))
			is ColonHead -> null
			is IndentHead -> tokenProcessor.flush(head.indent).tokenizer(
				parent,
				head(input(colon(false), "")))
		}

fun Tokenizer.pushOtherOrNull(char: Char): Tokenizer? =
	when (head) {
		is InputHead ->
			if (!char.isLetter()) null
			else
				tokenProcessor.tokenizer(
				parent,
				head(input(head.input.colon, head.input.name + char)))
		is ColonHead -> null
		is IndentHead ->
			tokenProcessor.flush(head.indent).tokenizer(
				parent,
				head(input(colon(false), "$char")))
	}

fun Processor<Token>.flush(indent: Indent): Processor<Token> =
	flush(indent.tab).updateIfNotNull(indent.previousOrNull) { flush(it) }

fun Processor<Token>.flush(tab: SpacesTab): Processor<Token> =
	process(token(closing)).updateIfNotNull(tab.previousOrNull) { flush(it) }

val Tokenizer.finish: Unit
	get() =
		if (!parent.isEmpty) fail("empty")
		else when (head) {
			is InputHead -> fail("input")
			is ColonHead -> fail("colon")
			is IndentHead -> tokenProcessor.flush(head.indent).run { Unit }
		}

val Tokenizer.charProcessor: Processor<Char>
	get() =
		processor { push(it) }