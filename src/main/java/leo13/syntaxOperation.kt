package leo13

sealed class SyntaxOperation

data class PlusSyntaxOperation(val plus: SyntaxPlus) : SyntaxOperation()
data class SetSyntaxOperation(val set: SyntaxSet) : SyntaxOperation()
data class SwitchSyntaxOperation(val switch: WordSwitch) : SyntaxOperation()
data class AsSyntaxOperation(val `as`: SyntaxAs) : SyntaxOperation()
data class OfSyntaxOperation(val of: SyntaxOf) : SyntaxOperation()

val SyntaxOperation.sentenceLine get() = TODO() as SentenceLine
