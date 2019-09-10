package leo13

sealed class SyntaxDefine

data class GivesSyntaxDefine(val gives: SyntaxDefineGives) : SyntaxDefine()
data class GivesSyntaxHas(val has: SyntaxDefineHas) : SyntaxDefine()

val SyntaxDefine.sentenceLine: SentenceLine
	get() =
		defineWord lineTo when (this) {
			is GivesSyntaxDefine -> gives.sentence
			is GivesSyntaxHas -> has.sentence
		}
