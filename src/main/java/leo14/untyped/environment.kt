package leo14.untyped

sealed class Environment
data class ContextEnvironment(val context: Context) : Environment()
data class QuotedEnvironment(val quoted: Quoted) : Environment()
data class Quoted(val environment: Environment)

val Context.environment: Environment get() = ContextEnvironment(this)
val Quoted.environment: Environment get() = QuotedEnvironment(this)
val Environment.quoted get() = Quoted(this)
