package leo14.syntax

import leo14.*

sealed class SyntaxToken

data class LiteralSyntaxToken(val literal: Literal): SyntaxToken()
data class BeginSyntaxToken(val name: Name): SyntaxToken()
object EndSyntaxToken: SyntaxToken()

fun syntaxToken(literal: Literal): SyntaxToken = LiteralSyntaxToken(literal)
fun beginSyntaxToken(name: Name): SyntaxToken = BeginSyntaxToken(name)
val endSyntaxToken: SyntaxToken = EndSyntaxToken
