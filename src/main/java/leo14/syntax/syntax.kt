package leo14.syntax

sealed class Syntax

data class ValueSyntax(val isKeyword: Boolean): Syntax()
data class TypeSyntax(val isKeyword: Boolean): Syntax()
object CommentSyntax: Syntax()

val valueSyntax: Syntax = ValueSyntax(false)
val valueKeywordKeyword: Syntax = ValueSyntax(true)
val typeSyntax: Syntax = TypeSyntax(false)
val typeKeywordSyntaxKeyword: Syntax = TypeSyntax(true)
val commentSyntax: Syntax = CommentSyntax
