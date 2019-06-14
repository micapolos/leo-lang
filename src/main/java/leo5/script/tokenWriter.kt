package leo5.script

import leo.base.Writer
import leo.base.write

fun Writer<Token>.write(script: Script): Writer<Token> =
	when (script) {
		is EmptyScript -> this
		is NonEmptyScript -> write(script.nonEmpty)
	}

fun Writer<Token>.write(nonEmpty: ScriptNonEmpty): Writer<Token> =
	this
		.write(nonEmpty.script)
		.write(nonEmpty.line)

fun Writer<Token>.write(line: Line) =
	this
		.write(token(begin(line.string)))
		.write(line.script)
		.write(token(end))
