package leo14.untyped.typed

data class Scope(val parentOrNull: Scope?, val definition: Definition)
