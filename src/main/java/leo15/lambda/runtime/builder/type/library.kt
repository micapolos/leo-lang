package leo15.lambda.runtime.builder.type

data class Library<V, T>(val scope: Scope<V, T>, val localBindingCount: Int)