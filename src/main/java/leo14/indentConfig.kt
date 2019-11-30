package leo14

import kotlin.math.max

data class IndentConfig(
	val maxDeepDepth: Int,
	val maxDepth: Int,
	val maxLength: Int,
	val initialMaxLength: Int)

val IndentConfig.begin
	get() =
		copy(maxDepth = max(0, maxDepth.dec()), maxLength = initialMaxLength)

val IndentConfig.beginDeep
	get() =
		copy(maxDeepDepth = max(0, maxDeepDepth.dec())).begin

val IndentConfig.next
	get() =
		copy(maxLength = max(0, maxLength.dec()))

val maxIndentConfig = IndentConfig(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
val limitedIndentConfig = IndentConfig(3, 8, 8, 8)
val defaultIndentConfig = maxIndentConfig

