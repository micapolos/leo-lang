package leo14.syntax

import leo.base.fold
import leo13.Empty
import leo13.empty
import leo14.Literal

data class Code(val kind: CodeKind, val body: CodeBody)

sealed class CodeBody
data class EmptyCodeBody(val empty: Empty) : CodeBody()
data class LinkCodeBody(val lhs: Code, val line: CodeLine) : CodeBody()

sealed class CodeLine
data class LiteralCodeLine(val literal: Literal) : CodeLine()
data class FieldCodeLine(val word: CodeWord, val rhs: Code) : CodeLine()

data class CodeWord(val kind: CodeNameKind, val string: String)

enum class CodeKind {
	VALUE,
	TYPE,
	COMMENT,
}

enum class CodeNameKind {
	WORD,
	KEYWORD,
}

val value = CodeKind.VALUE
val type = CodeKind.TYPE
val comment = CodeKind.COMMENT

fun code(kind: CodeKind, empty: Empty) = Code(kind, EmptyCodeBody(empty))
fun Code.plus(line: CodeLine): Code = Code(kind, LinkCodeBody(this, line))
fun CodeKind.code(vararg lines: CodeLine): Code = code(this, empty).fold(lines) { plus(it) }
val Literal.codeLine: CodeLine get() = LiteralCodeLine(this)
fun word(string: String) = CodeWord(CodeNameKind.WORD, string)
fun keyword(string: String) = CodeWord(CodeNameKind.KEYWORD, string)
infix fun CodeWord.to(rhs: Code): CodeLine = FieldCodeLine(this, rhs)

val Code.isEmpty get() = body.isEmpty
val CodeBody.isEmpty get() = this is EmptyCodeBody

val Code.isSimple: Boolean get() = body.isSimple
val CodeBody.isSimple
	get() =
		when (this) {
			is EmptyCodeBody -> true
			is LinkCodeBody -> lhs.isSimple && line.isSimple
		}

val CodeLine.isSimple
	get() =
		when (this) {
			is LiteralCodeLine -> true
			is FieldCodeLine -> rhs.isSimple
		}
