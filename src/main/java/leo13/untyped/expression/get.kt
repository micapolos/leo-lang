package leo13.untyped.expression

import leo13.getName
import leo13.script.lineTo
import leo13.script.script

data class Get(val name: String)

fun get(name: String) = Get(name)

val Get.scriptLine
	get() =
		getName lineTo script(name)