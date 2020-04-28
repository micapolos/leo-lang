package leo16

import leo13.Stack

data class Scope(val closureStack: Stack<Closure>)
data class Closure(val scope: Scope, val script: Script)

