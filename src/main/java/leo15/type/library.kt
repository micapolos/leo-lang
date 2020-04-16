package leo15.type

data class Library(val scope: Scope, val localBindingCount: Int)

val Library.clearLocal: Library get() = copy(localBindingCount = 0)