package leo21.token.typer

import leo21.token.processor.ChoiceCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TypeCompilerTokenProcessor
import leo21.type.Type

sealed class TypeParent
data class TypeNameTypeParent(val typeCompiler: TokenTypeCompiler, val name: String) : TypeParent()
data class ChoiceNameTypeParent(val choiceCompiler: TokenChoiceCompiler, val name: String) : TypeParent()

fun TypeParent.plus(type: Type): TokenProcessor =
	when (this) {
		is TypeNameTypeParent -> TypeCompilerTokenProcessor(typeCompiler.plus(name, type))
		is ChoiceNameTypeParent -> ChoiceCompilerTokenProcessor(choiceCompiler.plus(name, type))
	}
