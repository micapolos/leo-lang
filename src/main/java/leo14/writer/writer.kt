package leo14.writer

import leo.base.Indent
import leo.base.indent
import leo.base.runIf
import leo.base.string
import leo13.*
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.syntax.Syntax
import leo14.syntax.colored

data class Writer(
	val parent: WriterParent?,
	val lineStack: Stack<String>,
	val nameStack: Stack<String>,
	val column: Int,
	val indent: Indent)

data class WriterParent(
	val writer: Writer,
	val name: String)

val nullWriterParent: WriterParent? = null

fun Writer.parent(name: String) =
	WriterParent(this, name)

fun WriterParent?.writer(
	lineStack: Stack<String>,
	nameStack: Stack<String>,
	column: Int,
	indent: Indent) =
	Writer(this, lineStack, nameStack, column, indent)

fun Writer.write(syntax: Syntax): Writer? =
	when (syntax.token) {
		is LiteralToken ->
			copy(nameStack = nameStack.push(syntax.token.literal.toString() colored syntax.kind))
		is BeginToken ->
			this
				.parent(syntax.token.begin.string colored syntax.kind)
				.writer(
					lineStack = stack(),
					nameStack = stack(),
					column = column + syntax.token.begin.string.length + 2,
					indent = indent)
		is EndToken ->
			parent?.run {
				if (lineStack.isEmpty)
					if (nameStack.isEmpty)
						writer.pushName(name)
					else
						writer.collapseNames.pushLine(name + ": " + nameStack.toList().joinToString(" "))
				else
					writer.collapseNames.pushLine(name).pushIndentedLines(collapseNames.lineStack)
			}
	}

val Writer.string: String
	get() =
		if (parent == null)
			stringWithoutParent
		else
			parent.string + (if (lineStack.isEmpty) ": " else "\n") + stringWithoutParent

val Writer.stringWithoutParent: String
	get() =
		if (lineStack.isEmpty)
			nameStack.toList().joinToString(" ")
		else lineStack
			.runIf(!nameStack.isEmpty) { push(nameStack.toList().joinToString(" ")) }
			.map { "${indent.string}$this" }
			.toList()
			.joinToString("\n")

val WriterParent.string: String
	get() =
		if (writer.lineStack.isEmpty) writer.string + name
		else writer.string + "\n${writer.indent.string}" + name

val Writer.collapseNames
	get() =
		runIf(!nameStack.isEmpty) {
			copy(
				lineStack = lineStack.push(nameStack.toList().joinToString(" ")),
				nameStack = stack())
		}

fun Writer.pushName(name: String) =
	copy(nameStack = nameStack.push(name))

fun Writer.pushLine(line: String) =
	copy(lineStack = lineStack.push(line))

fun Writer.pushIndentedLines(newLineStack: Stack<String>) =
	copy(lineStack = lineStack.pushAll(newLineStack.map { 1.indent.string + this }))
