package leo13

import leo.base.Empty

sealed class Code
data class EmptyCode(val empty: Empty) : Code()