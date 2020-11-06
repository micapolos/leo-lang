package leo21.token.typer

import leo21.token.processor.TokenProcessor
import leo21.token.processor.TyperTokenProcessor
import leo21.type.Type

sealed class TypeParent
data class TyperNameTypeParent(val typer: TokenTyper, val name: String) : TypeParent()

fun TypeParent.plus(type: Type): TokenProcessor =
	when (this) {
		is TyperNameTypeParent -> TyperTokenProcessor(typer.plus(name, type))
	}
