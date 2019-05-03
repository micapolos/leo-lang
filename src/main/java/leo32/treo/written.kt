package leo32.treo

import leo.base.Stack
import leo.base.push
import leo.binary.Bit

data class Written(
	var stackOrNull: Stack<Bit>? = null)

fun Written.write(bit: Bit) {
	stackOrNull = stackOrNull.push(bit)
}
