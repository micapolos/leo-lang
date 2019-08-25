package leo13

import leo13.script.completedTokenStackOrNull
import leo13.script.push
import leo13.script.tokenizer
import leo9.Stack
import leo9.push
import leo9.stack

data class Tokens(val stack: Stack<Token>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = stack.asScriptLine("tokens") { asScriptLine }
}

val Stack<Token>.tokens get() = Tokens(this)
fun Tokens.plus(token: Token) = stack.push(token).tokens
fun tokens(vararg tokens: Token) = Tokens(stack(*tokens))
val String.unsafeTokens get() = tokenizer().push(this).completedTokenStackOrNull!!