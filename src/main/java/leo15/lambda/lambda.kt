package leo15.lambda

// TODO: Implement alternative Term representation, which is using
// LambdaVar instead of de Bruijn indx, and let resolveLambdaVars convert it
// to de Bruijn representation.
data class LambdaVar(val depth: Int)

fun lambdaVar(depth: Int) = LambdaVar(depth).valueTerm

fun Term.resolveLambdaVars(depth: Int): Term =
	when (this) {
		is ValueTerm ->
			if (value is LambdaVar) at(depth - value.depth - 1)
			else this
		is AbstractionTerm -> fn(body.resolveLambdaVars(depth.inc()), isRepeating)
		is ApplicationTerm -> lhs.resolveLambdaVars(depth).invoke(rhs.resolveLambdaVars(depth))
		is IndexTerm -> this
		is RepeatTerm -> rhs.resolveLambdaVars(depth).repeat
	}

val Term.resolveVars get() = resolveLambdaVars(0)

val lambdaDepthThreadLocal = ThreadLocal.withInitial { 0 }

fun lambda(isRepeating: Boolean, f: (Term) -> Term): Term {
	val v = LambdaVar(lambdaDepthThreadLocal.get()).valueTerm
	lambdaDepthThreadLocal.set(lambdaDepthThreadLocal.get().inc())
	val x = fn(f(v), isRepeating)
	lambdaDepthThreadLocal.set(lambdaDepthThreadLocal.get().dec())
	return x
}

fun lambda(f: (Term) -> Term): Term =
	lambda(false, f)

fun repeating(f: (Term) -> Term): Term =
	lambda(true, f)
