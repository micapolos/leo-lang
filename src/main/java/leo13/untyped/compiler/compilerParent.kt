package leo13.untyped.compiler

import leo13.untyped.TokenReader
import leo13.untyped.evaluator.EvaluatorLink
import leo13.untyped.evaluator.evaluator

sealed class CompilerParent

data class SelfCompilerParent(val link: CompilerLink) : CompilerParent()
data class SetCompilerParent(val link: SetCompilerLink) : CompilerParent()
data class CaseCompilerParent(val link: CaseCompilerLink) : CompilerParent()
data class EvaluatorCompilerParent(val link: EvaluatorLink) : CompilerParent()

val CompilerLink.parent get() = SelfCompilerParent(this)
val SetCompilerLink.parent get() = SetCompilerParent(this)
val CaseCompilerLink.parent get() = CaseCompilerParent(this)
val EvaluatorLink.parent get() = EvaluatorCompilerParent(this)

fun CompilerParent.plus(compiled: Compiled): TokenReader? =
	when (this) {
		is SelfCompilerParent -> link.compiler(compiled)
		is SetCompilerParent -> link.setCompiler(compiled)
		is CaseCompilerParent -> link.caseCompiler(compiled)
		is EvaluatorCompilerParent -> link.evaluator(compiled)
	}
