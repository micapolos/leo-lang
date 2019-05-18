package leo3

data class Call(
	val lhsTemplate: Template,
	val rhsTemplate: Template)

fun Call.apply(parameter: Parameter) =
	lhsTemplate.apply(parameter).let { lhsValue ->
		rhsTemplate.apply(parameter).let { rhsValue ->
			lhsValue.apply(rhsValue)
		}
	}