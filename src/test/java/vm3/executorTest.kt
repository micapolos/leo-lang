package vm3

import leo.base.assertEqualTo
import leo.base.assertNotNull
import vm3.dsl.data.array
import vm3.dsl.data.data
import vm3.dsl.data.struct
import vm3.dsl.type.f32
import vm3.dsl.type.get
import vm3.dsl.type.i32
import vm3.dsl.type.struct
import vm3.dsl.value.argument
import vm3.dsl.value.array
import vm3.dsl.value.dec
import vm3.dsl.value.get
import vm3.dsl.value.gives
import vm3.dsl.value.inc
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.times
import vm3.dsl.value.value
import kotlin.test.Test

class ExecutorTest {
	@Test
	fun compile() {
		i32.gives(argument).executor.assertNotNull
	}

	@Test
	fun bypass() {
		i32.gives(argument).executor.run {
			execute(123.data).assertEqualTo(123.data)
			execute(65536.data).assertEqualTo(65536.data)
		}
	}

	@Test
	fun i32() {
		i32.gives(argument.inc + argument.inc.inc)
			.executor
			.execute(10.data)
			.assertEqualTo(23.data)
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
	fun arr() {
		f32[2]
			.gives(argument)
			.executor
			.execute(array(10f.data, 20f.data))
			.assertEqualTo(array(10f.data, 20f.data))
	}

	@Test
	fun arrAt0() {
		f32[3]
			.gives(argument[0.value])
			.executor
			.execute(array(10f.data, 20f.data, 30f.data))
			.assertEqualTo(10f.data)
	}

	@Test
	fun arrAt1() {
		f32[3]
			.gives(argument[1.value])
			.executor
			.execute(array(10f.data, 20f.data, 30f.data))
			.assertEqualTo(20f.data)
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
	fun arrayOfArraysGet() {
		f32[2][3]
			.gives(argument[1.value][1.value])
			.executor
			.execute(
				array(
					array(10f.data, 20f.data),
					array(30f.data, 40f.data),
					array(50f.data, 60f.data)))
			.assertEqualTo(40f.data)
	}

	@Test
	fun arrayOfStructsGet() {
		struct("x" to f32, "y" to f32)[3]
			.gives(argument[1.value]["y"])
			.executor
			.execute(
				array(
					struct("x" to 10f.data, "y" to 20f.data),
					struct("x" to 30f.data, "y" to 40f.data),
					struct("x" to 50f.data, "y" to 60f.data)))
			.assertEqualTo(40f.data)
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
	fun structOfArraysGet() {
		struct(
			"first" to f32[2],
			"second" to f32[2])
			.gives(argument["second"][1.value])
			.executor
			.execute(
				struct(
					"first" to array(10f.data, 20f.data),
					"second" to array(30f.data, 40f.data)))
			.assertEqualTo(40f.data)
	}

	@Test
	fun arrayValue() {
		i32
			.gives(array(argument, argument))
			.executor
			.execute(10.data)
			.assertEqualTo(array(10.data, 10.data))
	}

	@Test
	fun arrayValueIncDec() {
		i32
			.gives(array(argument.inc, argument.dec))
			.executor
			.execute(10.data)
			.assertEqualTo(array(11.data, 9.data))
	}

	@Test
	fun arrayValuePlus() {
		i32
			.gives(array(argument, argument.plus(argument)))
			.executor
			.execute(10.data)
			.assertEqualTo(array(10.data, 20.data))
	}

	@Test
	fun structValue() {
		i32
			.gives(struct("x" to argument, "y" to argument))
			.executor
			.execute(10.data)
			.assertEqualTo(struct("x" to 10.data, "y" to 10.data))
	}

	@Test
	fun structValueIncDec() {
		i32
			.gives(struct("x" to argument.inc, "y" to argument.dec))
			.executor
			.execute(10.data)
			.assertEqualTo(struct("x" to 11.data, "y" to 9.data))
	}

	@Test
	fun structValuePlus() {
		i32
			.gives(struct("x" to argument, "y" to argument.plus(argument)))
			.executor
			.execute(10.data)
			.assertEqualTo(struct("x" to 10.data, "y" to 20.data))
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