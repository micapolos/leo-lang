package leo14.syntax

import leo.ansi
import leo.base.Indent
import leo.base.inc
import leo.base.indent
import leo.base.string
import leo.cyan
import leo.magenta
import leo.reset
import leo14.Literal

val Code.kindIndentString
	get() =
		kindString(0.indent)

fun Code.kindString(indent: Indent): String =
	body.string(indent)

fun CodeBody.string(indent: Indent): String =
	when (this) {
		is EmptyCodeBody -> ""
		is LinkCodeBody ->
			when (lhs.body) {
				is EmptyCodeBody ->
					line.string(indent)
				is LinkCodeBody ->
					if (lhs.isSimple) lhs.kindString(indent) + " " + line.string(indent)
					else lhs.kindString(indent) + "\n" + indent.string + line.string(indent)
			}
	}

fun CodeLine.string(indent: Indent): String =
	when (this) {
		is LiteralCodeLine -> literal.colorString
		is FieldCodeLine ->
			when (rhs.body) {
				is EmptyCodeBody -> word.string
				is LinkCodeBody ->
					if (rhs.body.lhs.isSimple) "${word.string}: ${rhs.kindString(indent)}"
					else "${word.string}\n${indent.inc.string}${rhs.kindString(indent.inc)}"
			}
	}

val CodeWord.string
	get() =
		kind
			.colorStringOrNull
			?.let { "$it$this${ansi.reset}" }
			?: string

val CodeNameKind.colorStringOrNull: String?
	get() =
		when (this) {
			CodeNameKind.WORD -> null
			CodeNameKind.KEYWORD -> ansi.magenta
		}

val Literal.colorString
	get() =
		"${ansi.cyan}$this${ansi.reset}"