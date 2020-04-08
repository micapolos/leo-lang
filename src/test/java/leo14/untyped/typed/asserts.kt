package leo14.untyped.typed

import leo.base.assertEqualTo

val <T> Compiled<T>.assertEvaluatedOnce: Compiled<T>
	get() = type.compiled(block.assertEvaluatedOnce)

val <T> Dynamic<T>.assertEvaluatesOnce: Dynamic<T>
	get() {
		var evaluated = false
		return dynamic {
			evaluated.assertEqualTo(false, "Evaluated twice")
			evaluated = true
			value
		}
	}

val <T> Block<T>.assertEvaluatedOnce: Block<T>
	get() =
		when (this) {
			is ConstantBlock -> this
			is DynamicBlock -> dynamic.assertEvaluatesOnce.block
		}