package leo14

import leo.ansi
import leo.bellChar
import leo.clear
import leo.home
import leo.java.lang.sttyPrivateMode
import leo13.linkOrNull
import leo13.push
import leo13.stack
import leo14.typed.compiler.CharLeo
import leo14.typed.compiler.emptyCharLeo
import leo14.typed.compiler.indentColorString
import leo14.typed.compiler.put
import java.io.InputStreamReader

const val printErrors = false

fun main() {
	sttyPrivateMode()
	var stack = stack<CharLeo>()
	var charLeo = emptyCharLeo.put(leonardoScript)
	var errorToPrint: Throwable? = null
	val reader = InputStreamReader(System.`in`)
	while (true) {
		print(charLeo)
		errorToPrint?.run {
			println("ERROR")
			printStackTrace()
		}
		val char = reader.read()
		if (char == -1) break
		if (char == 127) {
			if (errorToPrint != null) errorToPrint = null
			else stack.linkOrNull?.let { link ->
				charLeo = link.value
				stack = link.stack
			}
		} else if (errorToPrint == null) try {
			val newCharLeo = charLeo.put(char.toChar())
			stack = stack.push(charLeo)
			charLeo = newCharLeo
		} catch (e: RuntimeException) {
			print(bellChar)
			if (printErrors) errorToPrint = e
		}
	}
}

fun print(charLeo: CharLeo) {
	print("${ansi.clear}${ansi.home}")
//	println("context> ${charLeo.leo.promptString}")
	print("${charLeo.indentColorString}")
}