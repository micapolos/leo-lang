package leo13.script

data class WriterField<V, A>(val writer: Writer<A>, val fn: V.() -> A)

fun <V, A> field(writer: Writer<A>, fn: V.() -> A) = WriterField(writer, fn)
fun <V, A> WriterField<V, A>.scriptLine(value: V): ScriptLine = writer.scriptLine(value.fn())
