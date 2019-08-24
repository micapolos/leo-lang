package leo13.script

import leo.base.charSeq
import leo.base.fold
import leo.base.notNullIf
import leo13.*
import leo13.Script
import leo13.lineTo
import leo13.script
import leo9.*

data class Tokenizer(
	val tokenStack: Stack<Token>,
	val charStack: Stack<Char>,
	val errorOrNull: CharError?) {
	override fun toString() = asScript.toString()
	val asScript: Script
		get() = script(
			"tokens" lineTo tokenStack.asScript { "token" lineTo asScript },
			"chars" lineTo charStack.asScript { "char" lineTo script(toString() lineTo script()) },
			"error" lineTo errorOrNull.orNullAsScript { asScript })
}

data class CharError(val char: Char) {
	override fun toString() = asScript.toString()
	val asScript get() = script("char" lineTo script(char.toString() lineTo script()))
}

fun error(char: Char) = CharError(char)

fun tokenizer() = Tokenizer(stack(), stack(), null)
fun tokenizer(tokenStack: Stack<Token>, charStack: Stack<Char>, error: CharError?) =
	Tokenizer(tokenStack, charStack, error)

val Tokenizer.completedTokenStackOrNull
	get() =
		notNullIf(charStack.isEmpty && errorOrNull == null) { tokenStack }

fun Tokenizer.push(char: Char): Tokenizer =
	if (errorOrNull != null) this
	else when (char) {
		'(' -> open()
		')' -> close()
		else -> charPush(char)
	}

fun Tokenizer.push(string: String): Tokenizer =
	fold(string.charSeq) { push(it) }

fun Tokenizer.open(): Tokenizer =
	openingOrNull
		?.let { opening -> tokenPush(token(opening)) }
		?: put(error('('))

fun Tokenizer.close(): Tokenizer =
	notNullIf(charStack.isEmpty) {
		tokenPush(token(closing))
	} ?: put(error(')'))

fun Tokenizer.charPush(char: Char): Tokenizer =
	copy(charStack = charStack.push(char))

fun Tokenizer.tokenPush(token: Token): Tokenizer =
	copy(tokenStack = tokenStack.push(token), charStack = stack())

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

fun Tokenizer.put(error: CharError) =
	copy(errorOrNull = error)