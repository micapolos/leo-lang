package leo13.untyped.expression

import leo9.Stack

data class Expression(val opStack: Stack<Op>)