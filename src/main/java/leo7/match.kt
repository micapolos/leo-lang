package leo7

sealed class Match
data class FunctionMatch(val function: Function) : Match()
data class BodyMatch(val body: Body) : Match()
