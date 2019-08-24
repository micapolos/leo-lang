package leo13.script

import leo.base.charSeq
import leo.base.fold
import leo.base.notNullIf
import leo.base.orNull
import leo13.*
import leo13.Script
import leo13.lineTo
import leo13.script
import leo9.*

data class Tokenizer(
	val tokenStack: Stack<Token>,
	val charStack: Stack<Char>) {
	override fun toString() = asScript.toString()
	val asScript: Script
		get() = script(
			"tokens" lineTo tokenStack.asScript { "token" lineTo this.asScript },
			"chars" lineTo charStack.asScript { "char" lineTo script(toString() lineTo script()) })
}

fun tokenizer() = Tokenizer(stack(), stack())
fun tokenizer(tokenStack: Stack<Token>, charStack: Stack<Char>) = Tokenizer(tokenStack, charStack)
val Tokenizer.isComplete get() = charStack.isEmpty

fun Tokenizer.push(char: Char): Tokenizer? =
	when (char) {
		'(' -> open()
		')' -> close()
		else -> charPush(char)
	}

fun Tokenizer.push(string: String): Tokenizer? =
	orNull.fold(string.charSeq) { this?.push(it) }

fun Tokenizer.open(): Tokenizer? =
	openingOrNull?.let { opening ->
		Tokenizer(tokenStack.push(token(opening)), stack())
	}

fun Tokenizer.close(): Tokenizer? =
	notNullIf(charStack.isEmpty) {
		Tokenizer(tokenStack.push(token(closing)), stack())
	}

fun Tokenizer.charPush(char: Char): Tokenizer =
	Tokenizer(tokenStack, charStack.push(char))

val Tokenizer.string
	get() =
		StringBuilder()
			.fold(charStack.reverse) { append(it) }
			.toString()

val Tokenizer.openingOrNull: Opening?
	get() =
		string.let { string ->
			notNullIf(string.isNotEmpty()) { opening(string) }
		}

val Tokenizer.closingOrNull: Closing?
	get() =
		notNullIf(charStack.isEmpty) { closing }