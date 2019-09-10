package leo13

data class Script<out Header, out Line>(val header: Header, val lineListOrNull: List<Line>?)

fun <Header, Line> script(seed: Header, lineListOrNull: List<Line>?) =
	Script(seed, lineListOrNull)

fun <Header, Line> script(seed: Header, vararg lines: Line) =
	Script(seed, listOrNull(*lines))

fun <Header, Line> Script<Header, Line>.plus(line: Line) =
	Script(header, lineListOrNull.orNullPlus(line))
