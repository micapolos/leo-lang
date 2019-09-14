package leo13.token.reader

import leo.base.orNullIf
import leo.base.updateIfNotNull
import leo13.*
import leo13.base.Writer
import leo13.base.WriterObject
import leo13.base.writer
import leo13.script.CharLeo
import leo13.script.lineTo
import leo13.script.script
import leo13.token.Token
import leo13.token.closing
import leo13.token.opening
import leo13.token.token

data class Tokenizer(
	val tokenWriter: Writer<Token>,
	val parent: Parent,
	val head: Head) : WriterObject<CharLeo>() {
	override fun toString() = super.toString()
	override val writerScriptableName get() = "tokenizer"
	override val writerScriptableBody
		get() = script(
			tokenWriter.scriptableLine,
			parent.scriptableLine,
			head.scriptableLine)

	override fun writerWrite(leo: CharLeo): Writer<CharLeo> = push(leo.char)
	override val writerFinishWriting: Unit get() = finish
}

fun tokenizerWriter(tokenWriter: Writer<Token>): Writer<CharLeo> =
	tokenizer(tokenWriter)

fun tokenizer(tokenWriter: Writer<Token>, parent: Parent, head: Head): Tokenizer =
	Tokenizer(tokenWriter, parent, head)

fun tokenizer(): Tokenizer = tokenizer(writer())

fun tokenizer(tokenWriter: Writer<Token>): Tokenizer = tokenizer(
	tokenWriter,
	parent(),
	head(input(colon(false), "")))

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
				else tokenizer(
					tokenWriter
						.write(token(opening(head.input.name)))
						.write(token(closing)),
					parent,
					head(input(head.input.colon, "")))
			is ColonHead ->
				tokenizer(tokenWriter, parent, head(input(colon(true), "")))
			is IndentHead -> null
		}

val Tokenizer.pushTabOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead -> null
			is ColonHead -> null
			is IndentHead ->
				tokenizer(
					tokenWriter,
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
				else tokenizer(
					tokenWriter.write(token(opening(head.input.name))),
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
				else Tokenizer(
					tokenWriter.write(token(opening(head.input.name))),
					parent(),
					if (head.input.colon.boolean) head(parent.indentOrNull.orNullPlus(space).reverse)
					else head(parent.indentOrNull.orNullPlus(tab(space)).reverse))
			is ColonHead -> null
			is IndentHead -> null
		}

fun Tokenizer.pushOtherOrNull(char: Char): Tokenizer? =
	when (head) {
		is InputHead ->
			if (!char.isLetter()) null
			else tokenizer(
				tokenWriter,
				parent,
				head(input(head.input.colon, head.input.name + char)))
		is ColonHead -> null
		is IndentHead ->
			tokenizer(
				tokenWriter.flush(head.indent),
				parent,
				head(input(colon(false), "$char")))
	}

fun Writer<Token>.flush(indent: Indent): Writer<Token> =
	flush(indent.tab).updateIfNotNull(indent.previousOrNull) { flush(it) }

fun Writer<Token>.flush(tab: SpacesTab): Writer<Token> =
	write(token(closing)).updateIfNotNull(tab.previousOrNull) { flush(it) }

val Tokenizer.finish: Unit
	get() =
		if (!parent.isEmpty) fail("empty")
		else when (head) {
			is InputHead -> fail("input")
			is ColonHead -> fail("colon")
			is IndentHead -> tokenWriter.flush(head.indent).finishWriting
		}
