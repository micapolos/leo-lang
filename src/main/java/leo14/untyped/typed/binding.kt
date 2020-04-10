package leo14.untyped.typed

import leo.base.notNullIf
import leo14.untyped.isName

data class Binding(val keyType: Type, val valueCompiled: Compiled)

fun binding(keyType: Type, valueCompiled: Compiled) = Binding(keyType, valueCompiled)

fun Binding.apply(compiled: Compiled): Compiled? =
	notNullIf(keyType == compiled.type) {
		valueCompiled
	}

val Compiled.bindingOrNull: Binding?
	get() =
		type.matchInfix(isName) { rhs ->
			matchStatic {
				binding(this, rhs.compiled(expression))
			}
		}
