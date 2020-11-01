package leo21.scoped

import leo13.Stack

data class Scope(val bindingStack: Stack<Binding>)