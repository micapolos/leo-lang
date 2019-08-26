package leo13.token

import leo.base.charSeq
import leo.base.fold
import leo.base.notNullIf
import leo13.script.Scriptable
import leo13.script.asScriptLine
import leo13.script.lineTo
import leo13.script.orNullAsScriptLine
import leo9.*

data class Tokenizer(
	val tokenStack: Stack<Token>,
	val charStack: Stack<Char>,
	val errorOrNull: CharError?) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "tokenizer" lineTo leo13.script.script(
			tokenStack.asScriptLine("tokens") { asScriptLine },
			charStack.asScriptLine("chars") { "char" lineTo leo13.script.script(toString() lineTo leo13.script.script()) },
			errorOrNull.orNullAsScriptLine("error"))
}

data class CharError(val char: Char) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "error"
	override val scriptableBody get() = leo13.script.script(char.toString() lineTo leo13.script.script()) // TODO: Escape!!!
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