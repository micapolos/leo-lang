package leo13.type

sealed class Thunk

data class TypeThunk(val type: Type) : Thunk()
data class RecursionThunk(val recursion: Recursion) : Thunk()

fun thunk(type: Type): Thunk = TypeThunk(type)
fun thunk(recursion: Recursion): Thunk = RecursionThunk(recursion)
