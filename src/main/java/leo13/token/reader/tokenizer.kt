package leo13.token.reader

import leo.base.fold
import leo.base.updateIfNotNull
import leo13.*
import leo13.script.*
import leo13.token.*

data class Tokenizer(
	val tokens: Tokens,
	val parent: Parent,
	val head: Head,
	val status: Status) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "tokenizer"
	override val scriptableBody
		get() = script(
			tokens.scriptableLine,
			parent.scriptableLine,
			head.scriptableLine,
			status.scriptableLine)
}

fun tokenizer(tokens: Tokens, parent: Parent, head: Head, status: Status): Tokenizer =
	Tokenizer(tokens, parent, head, status)

fun tokenizer(): Tokenizer = tokenizer(
	tokens(),
	parent(),
	head(input(colon(false), "")),
	status(ok))

fun tokens(string: String): Tokens =
	tokenizer().push(string).finish

val String.tokens get() = tokens(this)

fun Tokenizer.unsafePush(string: String): Tokenizer =
	push(string).let { failIfError(it.status) }

fun Tokenizer.push(string: String): Tokenizer =
	fold(string) { push(it) }

fun Tokenizer.push(char: Char): Tokenizer =
	updateIfOk(status) {
		pushOrNull(char) ?: error(script(leo(char).scriptableLine))
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
					tokens
						.plus(token(opening(head.input.name)))
						.plus(token(closing)),
					parent,
					head(input(head.input.colon, "")),
					status)
			is ColonHead ->
				tokenizer(tokens, parent, head(input(colon(true), "")), status)
			is IndentHead -> null
		}

val Tokenizer.pushTabOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead -> null
			is ColonHead -> null
			is IndentHead ->
				tokenizer(
					tokens,
					parent.plus(head.indent.tab),
					head.indent.previousOrNull
						?.let { previous -> head(previous) }
						?: head(input(colon(false), "")),
					status)
		}

val Tokenizer.pushColonOrNull: Tokenizer?
	get() =
		when (head) {
			is InputHead ->
				if (head.input.name.isEmpty()) null
				else tokenizer(
					tokens.plus(token(opening(head.input.name))),
					if (head.input.colon.boolean) parent.plus(space)
					else parent.plus(tab(space)),
					head(colon),
					status)
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
					if (head.input.colon.boolean) head(parent.indentOrNull.orNullPlus(space).reverse)
					else head(parent.indentOrNull.orNullPlus(tab(space)).reverse),
					status)
			is ColonHead -> null
			is IndentHead -> null
		}

fun Tokenizer.pushOtherOrNull(char: Char): Tokenizer? =
	when (head) {
		is InputHead ->
			if (!char.isLetter()) null
			else tokenizer(
				tokens,
				parent,
				head(input(head.input.colon, head.input.name + char)),
				status)
		is ColonHead -> null
		is IndentHead ->
			tokenizer(
				tokens.flush(head.indent),
				parent,
				head(input(colon(false), "$char")),
				status)
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

fun Tokenizer.error(script: Script): Tokenizer =
	copy(status = status(leo13.error(script)))