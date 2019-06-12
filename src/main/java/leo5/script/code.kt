package leo5.script

var writtenScript = script()

sealed class Code {
	abstract val tailOrNull: Code?
}

data class Span(override val tailOrNull: Code?) : Code()
data class Block(override val tailOrNull: Code?) : Code()

tailrec fun Code.tailSize(acc: Int): Int {
	val tailOrNull = tailOrNull
	return if (tailOrNull == null) acc else tailOrNull.tailSize(acc.inc())
}

val Code.size get() = tailSize(1)

typealias CodeFn = () -> Unit

fun Code.code(string: String, vararg spans: Span): Code {
	val extension = writtenScript.wrap(string, spans.size)!!
	writtenScript = script(extension)
	return Block(this)
}

fun Code.code(string: String, fn: CodeFn): Code {
	val line = string lineTo writeScript(fn)
	writtenScript = writtenScript.extend(line)
	return Block(this)
}

fun Code.code(string: String, code: Code): Code {
	val extension = writtenScript.wrap(string, code.size)!!
	writtenScript = script(extension)
	return Block(this)
}

fun span(string: String, vararg spans: Span): Span {
	val script = script(writtenScript.wrap(string, spans.size)!!)
	writtenScript = script
	return Span(null)
}

fun span(string: String, code: Code): Span {
	val script = script(writtenScript.wrap(string, code.size)!!)
	writtenScript = script
	return Span(null)
}

fun span(string: String, fn: CodeFn): Span {
	val script = writtenScript.extend(string lineTo writeScript(fn))
	writtenScript = script
	return Span(null)
}

fun Script.write(fn: CodeFn): Script {
	val previouslyWrittenScript = writtenScript
	writtenScript = this
	try {
		fn()
		return writtenScript
	} finally {
		writtenScript = previouslyWrittenScript
	}
}

fun writeScript(fn: CodeFn): Script = script().write(fn)
