package leo14.syntax

import leo.*
import leo14.Literal

val Syntax.colorString get() =
	when (this) {
		is ValueSyntax -> if (isKeyword) ansi.magenta else ansi.white
		is TypeSyntax -> if (isKeyword) ansi.red else ansi.cyan
		is CommentSyntax -> ansi.brightBlack
	}

val Name.colorString get() =
	"${syntax.colorString}$string${ansi.reset}"

val Literal.colorString get() =
	"${ansi.red}$this${ansi.reset}"

val SyntaxToken.spacedColorString get() =
	when (this) {
		is LiteralSyntaxToken -> literal.colorString
		is BeginSyntaxToken -> "${name.colorString} "
		is EndSyntaxToken -> " "
	}

val SyntaxToken.coreColorString get() =
	when (this) {
		is LiteralSyntaxToken -> literal.colorString
		is BeginSyntaxToken -> "${name.colorString}("
		is EndSyntaxToken -> ")"
	}
