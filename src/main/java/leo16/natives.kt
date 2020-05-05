package leo16

import leo.ansi
import leo.magenta
import leo.reset
import leo15.intName
import leo15.numberName
import leo15.textName
import java.math.BigDecimal

val String.field: Field
	get() =
		textName(nativeField)

val BigDecimal.field: Field
	get() =
		numberName(nativeField)

val Int.field: Field
	get() =
		intName(nativeField)

val Any?.nativeString: String
	get() =
		"${ansi.magenta}«$this»${ansi.reset}"

fun <R : Any> nullIfThrowsException(fn: () -> R): R? =
	try {
		fn()
	} catch (e: Exception) {
		null
	}
