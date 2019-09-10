package leo13

sealed class Syntax

data class StartSyntax(val start: SyntaxStart) : Syntax()
data class LinkSyntax(val start: SyntaxLink) : Syntax()
