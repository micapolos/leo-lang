package leo16.term.chez

import leo.base.println
import leo.java.lang.exec
import leo13.push
import leo13.stack
import leo13.toList
import java.io.File

val String.eval: String
	get() {
		val file = File.createTempFile("script", ".ss")
		file.deleteOnExit()
		file.writeText("(display $this)")
		return file.run
	}

val File.run: String
	get() =
		stack<String>()
			.push("chez")
			.push("--optimize-level")
			.push("3")
			.push("--script")
			.push(absolutePath)
			.toList()
			.toTypedArray()
			.let { exec(*it) }

fun main() {
	"Hello, ".value
		.stringPlus("world!".value)
		.stringLength
		.intPlus(10000.value)
		.string
		.eval
		.println
}
