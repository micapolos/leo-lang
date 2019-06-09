package leo5

sealed class Function

data class ValueFunction(val value: Value) : Function()
data class SelfFunction(val self: Self) : Function()
data class ApplyFunction(val apply: Apply) : Function()

fun function(value: Value): Function = ValueFunction(value)
fun function(self: Self): Function = SelfFunction(self)
fun function(apply: Apply): Function = ApplyFunction(apply)

fun Function.invoke(argument: Value): Value = when (this) {
	is ValueFunction -> value
	is SelfFunction -> self.invoke(argument)
	is ApplyFunction -> apply.invoke(argument)
}

val Function.lhs get() = function(apply(this, op(leo5.lhs)))
val Function.rhs get() = function(apply(this, op(leo5.rhs)))
fun Function.plus(name: String, function: Function = function(value())) = function(apply(this, op(leo5.plus(name, function))))
fun Function.dispatch(vararg pairs: Pair<String, Function>) = function(apply(this, op(dispatcher(*pairs))))
