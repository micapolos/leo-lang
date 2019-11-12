package leo14.typed.compiler

import leo14.Begin
import leo14.Literal
import leo14.typed.Choice
import leo14.typed.Type
import leo14.typed.Typed
import leo14.typed.TypedArrow

sealed class Compiler<T>
data class TypedCompiler<T>(val parent: TypedParent<T>?, val typed: Typed<T>, val lit: Literal.() -> T) : Compiler<T>()
data class TypeCompiler<T>(val parent: TypeParent<T>?, val type: Type) : Compiler<T>()
data class ChoiceCompiler<T>(val parent: TypeCompiler<T>, val choice: Choice) : Compiler<T>()
data class MatchCompiler<T>(val parent: TypedCompiler<T>, val match: Match<T>) : Compiler<T>()
data class DeleteCompiler<T>(val parent: TypedCompiler<T>) : Compiler<T>()
data class AnyCompiler<T>(val parent: TypedCompiler<T>, val type: Type) : Compiler<T>()

sealed class TypedParent<T>
data class BeginTypedParent<T>(val typedCompiler: TypedCompiler<T>, val begin: Begin) : TypedParent<T>()
data class MatchTypedParent<T>(val matchCompiler: MatchCompiler<T>) : TypedParent<T>()
data class AnyDoesParent<T>(val typedCompiler: TypedCompiler<T>, val paramType: Type) : TypedParent<T>()
data class ApplyParent<T>(val typedCompiler: TypedCompiler<T>, val arrow: TypedArrow<T>) : TypedParent<T>()

sealed class TypeParent<T>
data class BeginTypeParent<T>(val typeCompiler: TypeCompiler<T>, val begin: Begin) : TypeParent<T>()
data class AsParent<T>(val typedCompiler: TypedCompiler<T>) : TypeParent<T>()
data class ChoiceTypeParent<T>(val choiceCompiler: ChoiceCompiler<T>, val begin: Begin) : TypeParent<T>()
data class AnyTypeParent<T>(val typedCompiler: TypedCompiler<T>) : TypeParent<T>()
