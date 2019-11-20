package leo14.syntax

import leo.*
import leo14.spacedString

val Kind.colorString
	get() =
	when (this) {
		is ValueKind -> if (isKeyword) ansi.magenta else ansi.reset
		is TypeKind -> if (isKeyword) ansi.red else ansi.cyan
		is CommentKind -> ansi.brightBlack
	}

fun String.colored(syntax: Kind) =
	"${syntax.colorString}$this${ansi.reset}"

val Syntax.spacedColorString
	get() =
		token.spacedString.colored(kind)

val Syntax.coreColorString
	get() =
		token.toString().colored(kind)

