package leo13.untyped.evaluator

import leo9.Stack

data class Context(val bindingStack: Stack<Evaluated>)