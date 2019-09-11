package leo13.untyped.compiler

import leo13.untyped.evaluator.EvaluatorLink

sealed class CompilerParent

data class SelfCompilerParent(val link: CompilerLink) : CompilerParent()
data class SetCompilerParent(val link: SetCompilerLink) : CompilerParent()
data class CaseCompilerParent(val link: CaseCompilerLink) : CompilerParent()
data class EvaluatorCompilerParent(val link: EvaluatorLink) : CompilerParent()