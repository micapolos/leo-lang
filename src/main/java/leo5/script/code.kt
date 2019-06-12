package leo5.script

var writtenScriptOrNull = null as Script?
val writtenScript get() = writtenScriptOrNull!!

sealed class Code
object Span: Code()
object Block: Code()

typealias CodeFn = () -> Unit

fun Code.code(string: String, vararg spans: Span): Code {
	writtenScriptOrNull = writtenScript.wrap(string, spans.size)!!
	return Block
}

fun Code.code(string: String, fn: CodeFn): Code {
	writtenScriptOrNull = writtenScript.extend(string lineTo writeScript(fn))
	return Block
}

fun span(string: String, vararg spans: Span): Span {
	writtenScriptOrNull = writtenScript.wrap(string, spans.size)!!
	return Span
}

fun span(string: String, fn: CodeFn): Span {
	writtenScriptOrNull = writtenScript.extend(string lineTo writeScript(fn))
	return Span
}

fun Script.write(fn: CodeFn): Script {
	val previouslyWrittenScriptOrNull = writtenScriptOrNull
	writtenScriptOrNull = this
	try {
		fn()
		return writtenScript
	} finally {
		writtenScriptOrNull = previouslyWrittenScriptOrNull
	}
}

fun writeScript(fn: CodeFn): Script = script().write(fn)
