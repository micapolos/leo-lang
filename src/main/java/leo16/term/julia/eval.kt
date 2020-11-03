package leo16.term.julia

import leo.base.println
import leo.java.lang.execExpectingExitCode
import leo13.push
import leo13.stack
import leo13.toList
import java.io.File

val String.eval: String
	get() {
		val file = File.createTempFile("script", ".jl")
		file.deleteOnExit()
		file.writeText("print($this)")
		return file.run
	}

val File.run: String
	get() =
		stack<String>()
			.push("julia")
			.push(absolutePath)
			.toList()
			.toTypedArray()
			.let { execExpectingExitCode(1, *it) }

fun main() {
	"5.0-3.0".eval.println
}
