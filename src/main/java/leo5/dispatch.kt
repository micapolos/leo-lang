package leo5

data class Dispatch(val body: Body, val bodyDictionary: BodyDictionary)

fun dispatch(body: Body, bodyDictionary: BodyDictionary) = Dispatch(body, bodyDictionary)
fun Dispatch.invoke(parameter: ValueParameter) = body.invoke(parameter).invokeDispatch(bodyDictionary, parameter)