package leo14.syntax

import leo14.Token

data class Syntax(val token: Token, val kind: Kind)

infix fun Token.of(kind: Kind) = Syntax(this, kind)
val Token.valueSyntax get() = this of valueKind
