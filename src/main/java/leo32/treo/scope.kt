package leo32.treo

sealed class Scope
data class TreoScope(var treo: Treo) : Scope()
data class SinkScope(val sink: Sink) : Scope()

fun scope(treo: Treo) = TreoScope(treo)
fun scope(sink: Sink) = SinkScope(sink)

fun Scope.invoke(put: Put) {
	when (this) {
		is TreoScope -> treo = treo.invoke(put.bit)
		is SinkScope -> sink.put(put.bit)
	}
}
