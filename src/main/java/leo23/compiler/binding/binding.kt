package leo23.compiler.binding

sealed class Binding
data class FunctionBinding(val function: BindingFunction) : Binding()
data class ConstantBinding(val function: BindingConstant) : Binding()
