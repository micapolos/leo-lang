package leo13.token.reader

import leo.base.fold
import leo.base.updateIfNotNull
import leo13.LeoObject
import leo13.base.Writer
import leo13.fail
import leo13.script.*
import leo13.space
import leo13.token.*

data class Tokenizer(
	val tokens: Tokens,
	val parent: Parent,
	val head: Head) : LeoObject(), Writer<CharLeo> {
	override fun toString() = super.toString()
	override val scriptableName get() = "reader"
	override val scriptableBody get() = script(tokens.scriptableLine, parent.scriptableLine, head.scriptableLine)

	override fun write(leo: CharLeo) = push(leo.char)
	override fun writeError(script: Script) = fail<CharLeo>(script).run { Unit }
	override val finishWriting get() = finish.run { Unit }
}

fun reader(tokens: Tokens, parent: Parent, head: Head) =
	Tokenizer(tokens, parent, head)

fun reader() = reader(tokens(), parent(), head(input(line(new), "")))

fun tokens(string: String): Tokens =
	reader().push(string).finish

val String.tokens get() = tokens(this)

object Colon

val colon = Colon

fun Tokenizer.push(string: String): Tokenizer =
	fold(string) { push(it) }

fun Tokenizer.push(char: Char): Tokenizer =
	pushOrNull(char)
		?: fail(scriptableBody.plus("push" lineTo script(leo(char).scriptableLine)))

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
				else Tokenizer(
					tokens
						.plus(token(opening(head.input.name)))
						.plus(token(closing)),
					parent,
					head(input(line(new), "")))
			is ColonHead ->
				reader(tokens, parent, head(input(line(same), "")))
			is IndentHead -> null
		}

val Tokenizer.pushTabOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead -> null
			is ColonHead -> null
			is IndentHead ->
				reader(
					tokens,
					parent.plus(head.indent.tab),
					head.indent.previousOrNull
						?.let { previous -> head(previous) }
						?: head(input(line(new), "")))
		}

val Tokenizer.pushColonOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead ->
				if (head.input.name.isEmpty()) null
				else reader(
					tokens.plus(token(opening(head.input.name))),
					when (head.input.line) {
						is SameLine -> parent.plus(space)
						is NewLine -> parent.plus(tab(space))
					},
					head(colon))
			is ColonHead -> null
			is IndentHead -> null
		}

val Tokenizer.pushNewlineOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead ->
				if (head.input.name.isEmpty()) null
				else Tokenizer(
					tokens.plus(token(opening(head.input.name))),
					parent(),
					when (head.input.line) {
						is SameLine -> head(parent.indentOrNull.orNullPlus(space).reverse)
						is NewLine -> head(parent.indentOrNull.orNullPlus(tab(space)).reverse)
					})
			is ColonHead -> null
			is IndentHead -> null
		}

fun Tokenizer.pushOtherOrNull(char: Char): Tokenizer? =
	when (head) {
		is InputHead ->
			if (!char.isLetter()) null
			else reader(tokens, parent, head(input(head.input.line, head.input.name + char)))
		is ColonHead -> null
		is IndentHead ->
			reader(
				tokens.flush(head.indent),
				parent,
				head(input(line(new), "$char")))
	}

fun Tokens.flush(indent: Indent): Tokens =
	flush(indent.tab).updateIfNotNull(indent.previousOrNull) { flush(it) }

fun Tokens.flush(tab: Tab): Tokens =
	plus(token(closing)).updateIfNotNull(tab.previousOrNull) { flush(it) }

val Tokenizer.finish: Tokens
	get() =
		finishOrNull ?: fail("finish")

val Tokenizer.finishOrNull: Tokens?
	get() =
		if (!parent.isEmpty) null
		else when (head) {
			is InputHead -> null
			is ColonHead -> null
			is IndentHead -> tokens.flush(head.indent)
		}
