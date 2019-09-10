package leo13.generic

sealed class ScriptStart<out Header, out Line>

data class HeaderScriptStart<Header, Line>(val header: Header) : ScriptStart<Header, Line>()
data class LineScriptStart<Header, Line>(val line: Line) : ScriptStart<Header, Line>()

fun <Header, Line> headerSeed(header: Header): ScriptStart<Header, Line> = HeaderScriptStart(header)
fun <Header, Line> lineSeed(line: Line): ScriptStart<Header, Line> = LineScriptStart(line)

val <Header, Line> ScriptStart<Header, Line>.headerOrNull get() = (this as? HeaderScriptStart)?.header
val <Header, Line> ScriptStart<Header, Line>.lineOrNull get() = (this as? LineScriptStart)?.line
