package leo14.syntax

import leo.base.Indent
import leo.base.indent
import leo.base.string
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken

data class Writer(
	val parent: WriterParent?,
	val code: Code,
	val column: Int,
	val indent: Indent)

data class WriterParent(
	val writer: Writer,
	val name: String)

val nullWriterParent: WriterParent? = null

fun Writer.parent(name: String) =
	WriterParent(this, name)

fun WriterParent?.writer(
	code: Code,
	column: Int,
	indent: Indent) =
	Writer(this, code, column, indent)

val emptyWriter = nullWriterParent.writer(value.code(), 0, 0.indent)

fun Writer.write(syntax: Syntax): Writer? =
	when (syntax.token) {
		is LiteralToken ->
			copy(code = code.plus(syntax.token.literal.codeLine))
		is BeginToken ->
			this
				.parent(syntax.token.begin.string colored syntax.kind)
				.writer(
					code = code.kind.code(),
					column = column + syntax.token.begin.string.length + 2,
					indent = indent)
		is EndToken ->
			parent?.run { writer.updateCode { plus(word(name) to value.code()) } }
	}

val Writer.indentString: String
	get() =
		if (parent == null) code.kindIndentString
		else parent.indentString + (if (code.isEmpty) ": " else "\n") + code.kindIndentString

val WriterParent.indentString: String
	get() =
		if (writer.code.isEmpty) writer.indentString + name
		else writer.indentString + "\n${writer.indent.string}" + name

fun Writer.updateCode(fn: Code.() -> Code) =
	copy(code = code.fn())
