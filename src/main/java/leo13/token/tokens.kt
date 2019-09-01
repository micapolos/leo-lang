package leo13.token

import leo13.LeoObject
import leo13.script.asScript
import leo9.Stack
import leo9.push
import leo9.stack

data class Tokens(val stack: Stack<Token>) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "tokens"
	override val scriptableBody get() = stack.asScript { scriptableLine }
}

val Stack<Token>.tokens get() = Tokens(this)
fun Tokens.plus(token: Token) = stack.push(token).tokens
fun tokens(vararg tokens: Token) = Tokens(stack(*tokens))
val String.unsafeTokens get() = tokenizer().push(this).completedTokensOrThrow