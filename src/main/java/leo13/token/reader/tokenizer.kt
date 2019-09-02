package leo13.token.reader

import leo.base.fold
import leo.base.updateIfNotNull
import leo13.LeoObject
import leo13.base.Writer
import leo13.colon
import leo13.fail
import leo13.script.*
import leo13.space
import leo13.token.*

data class Tokenizer(
	val tokens: Tokens,
	val parent: Parent,
	val head: Head) : LeoObject(), Writer<CharLeo> {
	override fun toString() = super.toString()
	override val scriptableName get() = "tokenizer"
	override val scriptableBody get() = script(tokens.scriptableLine, parent.scriptableLine, head.scriptableLine)

	override fun write(leo: CharLeo) = push(leo.char)
	override fun writeError(script: Script) = fail<CharLeo>(script).run { Unit }
	override val finishWriting get() = finish.run { Unit }
}

fun reader(tokens: Tokens, parent: Parent, head: Head) =
	Tokenizer(tokens, parent, head)

fun reader() = reader(tokens(), parent(), head(input(colon(false), "")))

fun tokens(string: String): Tokens =
	reader().push(string).finish

val String.tokens get() = tokens(this)

fun Tokenizer.push(string: String): Tokenizer =
	fold(string) { push(it) }

fun Tokenizer.push(char: Char): Tokenizer =
	pushOrNull(char)
		?: fail(script(scriptableLine).plus("push" lineTo script(leo(char).scriptableLine)))

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
					head(input(colon(false), "")))
			is ColonHead ->
				reader(tokens, parent, head(input(colon(true), "")))
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
						?: head(input(colon(false), "")))
		}

val Tokenizer.pushColonOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead ->
				if (head.input.name.isEmpty()) null
				else reader(
					tokens.plus(token(opening(head.input.name))),
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
				if (head.input.name.isEmpty()) null
				else Tokenizer(
					tokens.plus(token(opening(head.input.name))),
					parent(),
					head(parent.indentOrNull.orNullPlus(space).reverse))
			is ColonHead -> null
			is IndentHead -> null
		}

fun Tokenizer.pushOtherOrNull(char: Char): Tokenizer? =
	when (head) {
		is InputHead ->
			if (!char.isLetter()) null
			else reader(tokens, parent, head(input(head.input.colon, head.input.name + char)))
		is ColonHead -> null
		is IndentHead ->
			reader(
				tokens.flush(head.indent),
				parent,
				head(input(colon(false), "$char")))
	}

fun Tokens.flush(indent: Indent): Tokens =
	flush(indent.tab).updateIfNotNull(indent.previousOrNull) { flush(it) }

fun Tokens.flush(tab: SpacesTab): Tokens =
	plus(token(closing)).updateIfNotNull(tab.previousOrNull) { flush(it) }

val Tokenizer.finish: Tokens
	get() =
		finishOrNull ?: fail(script(scriptableLine).plus("finish" lineTo script()))

val Tokenizer.finishOrNull: Tokens?
	get() =
		if (!parent.isEmpty) null
		else when (head) {
			is InputHead -> null
			is ColonHead -> null
			is IndentHead -> tokens.flush(head.indent)
		}
