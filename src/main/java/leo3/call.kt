package leo3

data class Call(val lhs: Body, val rhs: BodyLine)

fun call(lhs: Body, rhs: BodyLine) = Call(lhs, rhs)

fun Call.apply(parameter: Parameter) =
	lhs.apply(parameter).let { lhsApplied ->
		rhs.apply(parameter).let { rhsApplied ->
			lhsApplied.call(rhsApplied)
		}
	}