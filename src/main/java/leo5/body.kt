package leo5

sealed class Body

data class ValueBody(val value: Value) : Body()
data class SelfBody(val self: Self) : Body()
data class ApplyBody(val apply: Apply) : Body()

fun body(value: Value): Body = ValueBody(value)
fun body(self: Self): Body = SelfBody(self)
fun body(apply: Apply): Body = ApplyBody(apply)

fun Body.invoke(argument: Value): Value = when (this) {
	is ValueBody -> value
	is SelfBody -> self.invoke(argument)
	is ApplyBody -> apply.invoke(argument)
}

val Body.lhs get() = body(apply(this, op(leo5.lhs)))
val Body.rhs get() = body(apply(this, op(leo5.rhs)))
fun Body.plus(name: String, body: Body = body(value())) = body(apply(this, op(leo5.plus(name, body))))
fun Body.dispatch(vararg pairs: Pair<String, Body>) = body(apply(this, op(dispatcher(*pairs))))
