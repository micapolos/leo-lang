package leo13.type

sealed class TypeThunk

data class TypeTypeThunk(val type: Type) : TypeThunk()
data class RecursionTypeThunk(val recursion: Recursion) : TypeThunk()

fun thunk(type: Type): TypeThunk = TypeTypeThunk(type)
fun thunk(recursion: Recursion): TypeThunk = RecursionTypeThunk(recursion)

