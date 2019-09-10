package leo13.generic

data class Script<out Header, out Line>(
	val start: ScriptStart<Header, Line>,
	val lineListOrNull: List<Line>?)

fun <Header, Line> script(start: ScriptStart<Header, Line>, lineListOrNull: List<Line>?) =
	Script(start, lineListOrNull)

fun <Header, Line> script(start: ScriptStart<Header, Line>, vararg lines: Line) =
	Script(start, listOrNull(*lines))

fun <Header, Line> Script<Header, Line>.plus(line: Line) =
	Script(start, lineListOrNull.orNullPlus(line))

