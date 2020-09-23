package vm3

import leo.base.assertEqualTo
import leo.base.assertNotNull
import vm3.dsl.data.data
import vm3.dsl.data.struct
import vm3.dsl.type.f32
import vm3.dsl.type.get
import vm3.dsl.type.struct
import vm3.dsl.value.argument
import vm3.dsl.value.get
import vm3.dsl.value.gives
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.switch
import vm3.dsl.value.times
import vm3.dsl.value.value
import kotlin.test.Test

class ExecutorTest {
	@Test
	fun compile() {
		f32.gives(argument).executor.assertNotNull
	}

	@Test
	fun bypass() {
		f32.gives(argument).executor.run {
			execute(123f.data).assertEqualTo(123f.data)
			execute(65536f.data).assertEqualTo(65536f.data)
		}
	}

	@Test
	fun f32() {
		f32
			.gives(argument + argument)
			.executor.run {
				execute(12.3f.data).assertEqualTo(24.6f.data)
			}
	}

	@Test
	fun struct() {
		struct("x" to f32, "y" to f32)
			.gives(argument)
			.executor
			.execute(struct("x" to 10f.data, "y" to 20f.data))
			.assertEqualTo(struct("x" to 10f.data, "y" to 20f.data))
	}

	@Test
	fun structGet() {
		struct("x" to f32, "y" to f32).gives(argument["y"])
			.executor
			.execute(struct("x" to 10f.data, "y" to 20f.data))
			.assertEqualTo(20f.data)
	}

	@Test
	fun structOfStructsGet() {
		struct(
			"first" to struct("x" to f32, "y" to f32),
			"second" to struct("z" to f32, "w" to f32))
			.gives(argument["second"]["w"])
			.executor
			.execute(
				struct(
					"first" to struct("x" to 10f.data, "y" to 20f.data),
					"second" to struct("z" to 30f.data, "w" to 40f.data)))
			.assertEqualTo(40f.data)
	}

	@Test
	fun structValue() {
		f32
			.gives(struct("x" to argument, "y" to argument))
			.executor
			.execute(10f.data)
			.assertEqualTo(struct("x" to 10f.data, "y" to 10f.data))
	}

	@Test
	fun structValuePlus() {
		f32
			.gives(struct("x" to argument, "y" to argument.plus(argument)))
			.executor
			.execute(10f.data)
			.assertEqualTo(struct("x" to 10f.data, "y" to 20f.data))
	}

	@Test
	fun program() {
		val radius = argument.get("circle").get("radius")
		val pi = Math.PI.toFloat().value
		val area = radius.times(radius).times(pi)

		struct(
			"circle" to struct(
				"radius" to f32,
				"center" to struct(
					"point" to struct(
						"x" to f32,
						"y" to f32))))
			.gives(area)
			.executor
			.execute(
				struct(
					"circle" to struct(
						"radius" to 10f.data,
						"center" to struct(
							"point" to struct(
								"x" to 20f.data,
								"y" to 30f.data)))))
			.assertEqualTo(10f.times(10f).times(Math.PI.toFloat()).data)
	}
}