package leo5

sealed class Interpreter
data class ErrorInterpreter(val body: Body) : Interpreter()
data class ValueInterpreter(val value: Value) : Interpreter()