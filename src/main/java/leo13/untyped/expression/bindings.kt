package leo13.untyped.expression

import leo13.untyped.evaluator.Value
import leo9.Stack

data class Bindings(val valueStack: Stack<Value>)