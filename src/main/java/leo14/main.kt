package leo14

import leo.ansi
import leo.clear
import leo.home
import leo.java.lang.sttyPrivateMode
import leo13.linkOrNull
import leo13.push
import leo13.stack
import leo14.typed.compiler.CharLeo
import leo14.typed.compiler.emptyCharLeo
import leo14.typed.compiler.put
import leo14.typed.compiler.string

fun main() {
	sttyPrivateMode()
	var stack = stack<CharLeo>()
	var charLeo = emptyCharLeo
	print("${ansi.clear}${ansi.home}leo14> ")
	while (true) {
		val char = System.`in`.read()
		print("${ansi.clear}${ansi.home}leo14> ")
		if (char == -1) break
		if (char == 127) {
			stack.linkOrNull?.let { link ->
				charLeo = link.value
				stack = link.stack
				print(charLeo.string)
			}
		} else try {
			val newCharLeo = charLeo.put(char.toChar())
			stack = stack.push(charLeo)
			charLeo = newCharLeo
			print(charLeo.string)
		} catch (e: RuntimeException) {
			println(charLeo.string)
			println("ERROR")
			e.printStackTrace()
		}
	}
}