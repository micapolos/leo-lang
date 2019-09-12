package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.getName

data class Get(val name: String)

fun get(name: String) = Get(name)

val Get.scriptLine
	get() =
		getName lineTo script(name)