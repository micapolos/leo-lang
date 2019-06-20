package leo5.script

fun Appendable.append(script: Script): Appendable =
	when (script) {
		is EmptyScript -> this
		is NonEmptyScript -> append(script.nonEmpty)
	}

fun Appendable.append(line: Line) =
	append(line.string).append('(').append(line.script).append(')')

fun Appendable.append(scriptNonEmpty: ScriptNonEmpty) =
	append(scriptNonEmpty.line).append(scriptNonEmpty.script)
