package leo13.untyped.compiler

data class CaseCompilerLink(val caseCompiler: CaseCompiler, val name: String)

infix fun CaseCompiler.linkTo(name: String) = CaseCompilerLink(this, name)
