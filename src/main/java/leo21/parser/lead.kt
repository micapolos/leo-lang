package leo21.parser

import leo13.Stack

data class Lead(
	val leadingIndentStack: Stack<Int>,
	val trailingIndentStack: Stack<Int>
)

