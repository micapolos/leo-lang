package leo13.type

data class TypeTrace(val lhsOrNull: TypeTrace?, val type: Type)

val Type.trace get() = TypeTrace(null, this)
fun TypeTrace?.plus(type: Type) = TypeTrace(this, type)

fun TypeTrace.applyOrNull(recursion: Recursion): TypeTrace? =
	lhsOrNull?.let { lhs ->
		if (recursion.lhsOrNull == null) lhs
		else applyOrNull(recursion.lhsOrNull)
	}

fun TypeTrace.applyOrNull(thunk: TypeThunk): TypeTrace? =
	when (thunk) {
		is TypeTypeThunk -> plus(thunk.type)
		is RecursionTypeThunk -> applyOrNull(thunk.recursion)
	}

fun TypeTrace.rhsOrNull(name: String): TypeTrace? =
	type.rhsThunkOrNull(name)?.let { thunk ->
		applyOrNull(thunk)
	}

fun TypeTrace.accessOrNull(name: String): TypeTrace? =
	type.onlyLineOrNull?.let { line ->
		applyOrNull(line.rhs)?.let { structTrace ->
			structTrace.type.rhsThunkOrNull(name)?.let { rhsThunk ->
				structTrace.lhsOrNull.plus(type(name lineTo rhsThunk))
			}
		}
	}
