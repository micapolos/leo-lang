package leo16

import leo.ansi
import leo.magenta
import leo.reset
import leo16.names.*
import java.math.BigDecimal

val String.field: Field
	get() =
		_text(nativeField)

val BigDecimal.field: Field
	get() =
		_number(nativeField)

val Int.field: Field
	get() =
		_int(nativeField)

val Any?.nativeString: String
	get() =
		"${ansi.magenta}«$this»${ansi.reset}"

fun <R : Any> nullIfThrowsException(fn: () -> R): R? =
	try {
		fn()
	} catch (e: Exception) {
		null
	}
