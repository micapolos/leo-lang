package leo13

data class Function(val parameter: PatternParameter, val body: FunctionBody)
data class FunctionBody(val expr: Expr, val returns: PatternReturns)
data class PatternParameter(val type: Type)
data class PatternReturns(val type: Type)

fun function(parameter: PatternParameter, body: FunctionBody) = Function(parameter, body)
fun parameter(type: Type) = PatternParameter(type)
fun returns(type: Type) = PatternReturns(type)
fun body(expr: Expr, returns: PatternReturns) = FunctionBody(expr, returns)

//fun functionBody(script: Script, parameter: PatternParameter): FunctionBody =
//	when (script) {
//		is EmptyScript -> body(expr(empty), returns(type()))
//		is LinkScript -> functionBody(script.link, parameter)
//	}
//
//fun functionBody(link: ScriptLink, parameter: PatternParameter): FunctionBody =
//	null
//		?: givenTypedOrNull(link, parameter)
//		?: TODO()
//
//fun givenTypedOrNull(link: ScriptLink, parameter: PatternParameter) =
//	notNullIf(link == link(script(), "given" lineTo script())) {
//		body(expr(argument), returns(parameter.type))
//	}
//
//fun Function.bodyOrNull(parameter: PatternParameter) =
//	notNullIf(this.parameter == parameter) { body }
