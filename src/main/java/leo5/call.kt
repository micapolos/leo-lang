package leo5

data class Call(val body: Body, val parameter: BodyParameter)

fun call(body: Body, parameter: BodyParameter) = Call(body, parameter)
fun Call.invoke(valueParameter: ValueParameter) =
	body.invoke(parameter(parameter.body.invoke(valueParameter)))
