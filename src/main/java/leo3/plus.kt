package leo3

data class Plus(val body: Body, val bodyLine: BodyLine)

fun plus(lhs: Body, rhs: BodyLine) = Plus(lhs, rhs)
fun Plus.apply(parameter: Parameter) =
	body.apply(parameter).let { value ->
		bodyLine.apply(parameter).let { valueLine ->
			value.plus(valueLine)
		}
	}
