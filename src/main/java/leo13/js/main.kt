package leo13.js

import java.awt.Desktop
import java.io.File

fun main(args: Array<String>) {
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
	file.writeText(typed.expression.code.jsInHtml)
	val uri = file.toURI()
	println(uri)
	file.deleteOnExit()
	Desktop.getDesktop().browse(uri)
}
