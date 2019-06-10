package leo5

sealed class Body

data class ValueBody(val value: Value) : Body()
data class ArgumentBody(val argument: Argument) : Body()
data class LhsBody(val lhs: Lhs) : Body()
data class RhsBody(val rhs: Rhs) : Body()
data class ApplicationBody(val application: BodyApplication) : Body()
data class DispatchBody(val dispatch: Dispatch) : Body()
data class CallBody(val call: Call) : Body()

fun body(value: Value): Body = ValueBody(value)
fun body(self: Argument): Body = ArgumentBody(self)
fun body(lhs: Lhs): Body = LhsBody(lhs)
fun body(rhs: Rhs): Body = RhsBody(rhs)
fun body(application: BodyApplication): Body = ApplicationBody(application)
fun body(dispatch: Dispatch): Body = DispatchBody(dispatch)
fun body(call: Call): Body = CallBody(call)

val Body.lhs get() = body(lhs(this))
val Body.rhs get() = body(rhs(this))
fun Body.plus(line: BodyLine) = body(application(this, line))
fun Body.dispatch(dictionary: BodyDictionary) = body(dispatch(this, dictionary))
fun Body.call(parameter: BodyParameter) = body(call(this, parameter))

fun Body.invoke(parameter: ValueParameter): Value = when (this) {
	is ValueBody -> value
	is ArgumentBody -> argument.invoke(parameter)
	is LhsBody -> lhs.invoke(parameter)
	is RhsBody -> rhs.invoke(parameter)
	is ApplicationBody -> application.invoke(parameter)
	is DispatchBody -> dispatch.invoke(parameter)
	is CallBody -> call.invoke(parameter)
}
