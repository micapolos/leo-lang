package leo14.typed.compiler

import leo14.Begin
import leo14.typed.*

sealed class Compiler<T>
data class TypedCompiler<T>(val parent: TypedParent<T>?, val context: Context<T>, val typed: Typed<T>) : Compiler<T>()
data class TypeCompiler<T>(val parent: TypeParent<T>?, val type: Type) : Compiler<T>()
data class ChoiceCompiler<T>(val parent: TypeCompiler<T>, val choice: Choice) : Compiler<T>()
data class NativeCompiler<T>(val parent: TypeCompiler<T>) : Compiler<T>()
data class MatchCompiler<T>(val parent: TypedCompiler<T>, val match: Match<T>) : Compiler<T>()
data class ActionCompiler<T>(val parent: TypedCompiler<T>) : Compiler<T>()
data class ActionItCompiler<T>(val parent: TypedCompiler<T>, val type: Type) : Compiler<T>()
data class ActionItDoesCompiler<T>(val parent: TypedCompiler<T>, val action: Action<T>) : Compiler<T>()
data class RememberCompiler<T>(val parent: TypedCompiler<T>) : Compiler<T>()
data class RememberItCompiler<T>(val parent: TypedCompiler<T>, val type: Type) : Compiler<T>()
data class RememberItIsCompiler<T>(val parent: TypedCompiler<T>, val action: Action<T>) : Compiler<T>()
data class RememberItDoesCompiler<T>(val parent: TypedCompiler<T>, val action: Action<T>) : Compiler<T>()

sealed class TypedParent<T>
data class BeginTypedParent<T>(val typedCompiler: TypedCompiler<T>, val begin: Begin) : TypedParent<T>()
data class MatchTypedParent<T>(val matchCompiler: MatchCompiler<T>) : TypedParent<T>()
data class ActionItDoesParent<T>(val typedCompiler: TypedCompiler<T>, val itType: Type) : TypedParent<T>()
data class DoParent<T>(val typedCompiler: TypedCompiler<T>, val arrow: TypedArrow<T>) : TypedParent<T>()
data class GiveParent<T>(val typedCompiler: TypedCompiler<T>) : TypedParent<T>()
data class RememberItIsParent<T>(val typedCompiler: TypedCompiler<T>, val itType: Type) : TypedParent<T>()
data class RememberItDoesParent<T>(val typedCompiler: TypedCompiler<T>, val itType: Type) : TypedParent<T>()

sealed class TypeParent<T>
data class BeginTypeParent<T>(val typeCompiler: TypeCompiler<T>, val begin: Begin) : TypeParent<T>()
data class AsParent<T>(val typedCompiler: TypedCompiler<T>) : TypeParent<T>()
data class ChoiceTypeParent<T>(val choiceCompiler: ChoiceCompiler<T>, val begin: Begin) : TypeParent<T>()
data class ActionItParent<T>(val actionCompiler: ActionCompiler<T>) : TypeParent<T>()
data class RememberItParent<T>(val rememberCompiler: RememberCompiler<T>) : TypeParent<T>()
