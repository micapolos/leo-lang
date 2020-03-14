package leo14.untyped

import sun.font.Script

sealed class Code
data class ScriptCode(val script: Script) : Code()
data class PrimitiveCode(val primitive: Primitive) : Code()