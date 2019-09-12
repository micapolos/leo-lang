package leo13.untyped.expression

import leo9.Stack

data class Bound(val previousStack: Stack<Previous>)
