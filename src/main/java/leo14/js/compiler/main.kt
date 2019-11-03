package leo13.js.compiler

import leo14.begin
import leo14.end
import leo14.lambda.js.code
import leo14.token
import java.awt.Desktop
import java.io.File

fun String.htmlOpen() {
	val file = File.createTempFile("index", ".html")
	file.writeText(this)
	val uri = file.toURI()
	file.deleteOnExit()
	Desktop.getDesktop().browse(uri)
}

fun main() {
	val file = File.createTempFile("index", ".html")
	val typed = compile {
		this
			.write(token(begin("native")))
			.write(token("console"))
			.write(token(end))
			.write(token(begin("call")))
			.write(token("log"))
			.write(token(begin("with")))
			.write(token("jajeczko"))
			.write(token(end))
			.write(token(end))
	}
	file.writeText(typed.value.code.jsInHtml)
	val uri = file.toURI()
	println(uri)
	file.deleteOnExit()
	Desktop.getDesktop().browse(uri)
}
