package leo5.script

sealed class Token

data class BeginToken(val begin: Begin): Token()
data class EndToken(val end: End): Token()

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)

val Token.beginOrNull get() = (this as? BeginToken)?.begin
val Token.endOrNull get() = (this as? EndToken)?.end
