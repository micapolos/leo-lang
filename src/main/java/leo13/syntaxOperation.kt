package leo13

sealed class SyntaxOperation

data class PlusSyntaxOperation(val plus: SyntaxPlus) : SyntaxOperation()
data class GetSyntaxOperation(val get: SyntaxGet) : SyntaxOperation()
data class SetSyntaxOperation(val set: SyntaxSet) : SyntaxOperation()
data class SwitchSyntaxOperation(val switch: SyntaxSwitch) : SyntaxOperation()
