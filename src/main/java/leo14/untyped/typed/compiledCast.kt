package leo14.untyped.typed

import leo.base.notNullIf

// TODO: Implement
fun Compiled.as_(targetType: Type): Compiled? =
	notNullIf(type == targetType) { this }