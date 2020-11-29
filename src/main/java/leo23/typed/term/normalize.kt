package leo23.typed.term

import leo23.type.onlyNameOrNull

val StackCompiled.normalize: StackCompiled
	get() =
		normalizeOrNull ?: this

val StackCompiled.normalizeOrNull: StackCompiled?
	get() =
		linkOrNull?.let { (lhs, rhs) ->
			rhs.t.onlyNameOrNull?.let { name ->
				lhs.make(name).stack
			}
		}