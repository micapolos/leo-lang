package leo16

import leo13.Stack

data class Context(val openingStack: Stack<Opening>)
data class Opening(val reader: Reader, val word: String)
