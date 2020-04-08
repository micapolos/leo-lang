package leo14.untyped.typed

data class Matching(val compiled: Compiled<*>)
data class Remaining(val remaining: Compiled<*>)
data class Match(val matching: Matching, val remaining: Remaining)

val Compiled<*>.matching get() = Matching(this)
val Compiled<*>.remaining get() = Remaining(this)
val Compiled<*>.mismatch get() = nothingCompiled.matching and remaining
infix fun Matching.and(remaining: Remaining) = Match(this, remaining)

fun Type.match(compiled: Compiled<*>, recursiveOrNull: TypeRecursive?): Match =
	when (this) {
		EmptyType -> emptyMatch(compiled)
		AnythingType -> anythingMatch(compiled)
		NothingType -> nothingMatch(compiled)
		is LinkType -> link.match(compiled, recursiveOrNull)
		is AlternativeType -> alternative.match(compiled, recursiveOrNull)
		is FunctionType -> function.match(compiled, recursiveOrNull)
		is RecursiveType -> recursive.match(compiled, recursiveOrNull)
		RecurseType -> recurseMatch(compiled, recursiveOrNull)
	}

fun emptyMatch(compiled: Compiled<*>): Match =
	when (compiled.type) {
		EmptyType -> compiled.matching and nothingCompiled.remaining
		else -> nothingCompiled.matching and compiled.remaining
	}

fun anythingMatch(compiled: Compiled<*>): Match =
	compiled.matching and nothingCompiled.remaining

fun nothingMatch(compiled: Compiled<*>): Match =
	nothingCompiled.matching and compiled.remaining

fun TypeLink.match(compiled: Compiled<*>, recursiveOrNull: TypeRecursive?): Match =
	if (compiled.type !is LinkType) nothingCompiled.matching and compiled.remaining
	else match(compiled.type.link, compiled.expression, recursiveOrNull)

fun TypeLink.match(compiledTypeLink: TypeLink, expression: Expression<*>, recursiveOrNull: TypeRecursive?): Match =
	if (compiledTypeLink.lhs.isStatic || compiledTypeLink.line.isStatic) TODO()
	else TODO()

fun TypeAlternative.match(compiled: Compiled<*>, recursiveOrNull: TypeRecursive?): Match =
	TODO()

fun TypeFunction.match(compiled: Compiled<*>, recursiveOrNull: TypeRecursive?): Match =
	if (compiled.type !is FunctionType) compiled.mismatch
	else match(compiled.type.function, compiled.expression, recursiveOrNull)

fun TypeFunction.match(compiledFunction: TypeFunction, expression: Expression<*>, recursiveOrNull: TypeRecursive?): Match =
	if (from == compiledFunction.from && to == compiledFunction.to) TODO()
	else compiledFunction.type.compiled(expression).mismatch

fun TypeRecursive.match(compiled: Compiled<*>, recursiveOrNull: TypeRecursive?): Match =
	type.match(compiled, this)

fun recurseMatch(compiled: Compiled<*>, recursiveOrNull: TypeRecursive?): Match =
	recursiveOrNull!!.type.match(compiled, recursiveOrNull)