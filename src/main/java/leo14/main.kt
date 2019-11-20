package leo14

import leo.ansi
import leo.bellChar
import leo.clear
import leo.home
import leo.java.lang.sttyPrivateMode
import leo13.linkOrNull
import leo13.push
import leo13.stack
import leo14.typed.compiler.*

fun main() {
	sttyPrivateMode()
	var stack = stack<CharLeo>()
	var charLeo = emptyCharLeo
	var error: Throwable? = null
	while (true) {
		print(charLeo)
		error?.run {
			print(bellChar)
			println("ERROR")
			printStackTrace()
		}
		error = null
		val char = System.`in`.read()
		if (char == -1) break
		if (char == 127) {
			stack.linkOrNull?.let { link ->
				charLeo = link.value
				stack = link.stack
			}
		} else try {
			val newCharLeo = charLeo.put(char.toChar())
			stack = stack.push(charLeo)
			charLeo = newCharLeo
		} catch (e: RuntimeException) {
			error = e
		}
	}
}

fun print(charLeo: CharLeo) {
	println("${ansi.clear}${ansi.home}context> ${charLeo.leo.promptString}")
	print("leo> ${charLeo.coreColorString}")
}