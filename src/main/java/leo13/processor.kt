package leo13

interface Processor<V> : Scripting {
	fun process(value: V): Processor<V>
}
