package leo13.untyped.evaluator

import leo13.untyped.value.Value
import leo9.Stack
import leo9.stack

data class Context(val bindingStack: Stack<Value>)
val Stack<Value>.context get() = Context(this)
fun context(vararg values: Value) = stack(*values).context
