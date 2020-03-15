package leo14.untyped

sealed class ValueReader

data class ResolverValueReader(val resolver: Resolver) : ValueReader()
data class ProgramValueReader(val program: Program) : ValueReader()

fun valueReader(resolver: Resolver): ValueReader = ResolverValueReader(resolver)
fun valueReader(program: Program): ValueReader = ProgramValueReader(program)

fun ValueReader.read(value: Value) =
	when (this) {
		is ResolverValueReader -> resolver.apply(value)
		is ProgramValueReader -> program.plus(value)
	}
