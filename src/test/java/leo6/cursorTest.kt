package leo6

import leo.base.assertEqualTo
import kotlin.test.Test

class CursorTest {
	@Test
	fun construction() {
		cursor()
			.at(word("one"))
			.assertEqualTo(null)

		cursor()
			.begin("one")
			.begin("1")
			.end!!
			.end!!
			.at(word("one"))
			.assertEqualTo(cursor().begin("one").with(script("1")))

		cursor()
			.begin("one")
			.begin("1")
			.end!!
			.end!!
			.begin("plus")
			.at(word("one"))
			.assertEqualTo(cursor().begin("one").with(script("1")))

		cursor()
			.begin("one")
			.begin("1")
			.end!!
			.end!!
			.begin("plus")
			.begin("one")
			.begin("2")
			.end!!
			.end!!
			.at(word("one"))
			.assertEqualTo(
				cursor()
					.begin("one")
					.begin("1")
					.end!!
					.end!!
					.begin("plus")
					.begin("one")
					.with(script("2")))

	}

	@Test
	fun atPath() {
		val circle = cursor(
			script(
				"circle" lineTo script(
					"radius" lineTo "12",
					"center" lineTo script(
						"x" lineTo "13",
						"y" lineTo "14"))))

		circle.at(path()).assertEqualTo(circle)
		circle.at(path("circle", "radius"))!!.script.assertEqualTo(script("12"))
		circle.at(path("circle", "center", "x"))!!.script.assertEqualTo(script("13"))
		circle.at(path("circle", "center", "y"))!!.script.assertEqualTo(script("14"))

		circle
			.at(path("circle", "center"))!!
			.at(path("radius"))!!
			.script
			.assertEqualTo(script("12"))

		val list = cursor(
			script(
				"name" lineTo script("first" lineTo "michal"),
				"name" lineTo script("last" lineTo "pociecha")))

		list
			.at(path("name", "first"))!!
			.script
			.assertEqualTo(script("michal"))

		list
			.at(path("name", "last"))!!
			.script
			.assertEqualTo(script("pociecha"))
	}

	@Test
	fun rootPathTo() {
		val circle = cursor(
			script(
				"circle" lineTo script(
					"radius" lineTo "12",
					"center" lineTo script(
						"x" lineTo "13",
						"y" lineTo "14"))))

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.rootPathTo(path("x"))!!
			.assertEqualTo(path("circle", "center", "x"))

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.rootPathTo(path("x"))!!
			.assertEqualTo(path("circle", "center", "x"))

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.rootPathTo(path("center"))
			.assertEqualTo(null)

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.rootPathTo(path("radius"))
			.assertEqualTo(path("circle", "radius"))

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.rootPathTo(path("circle"))
			.assertEqualTo(null)
	}


	@Test
	fun recursiveRootPathTo() {
		val circle = cursor(
			script(
				"circle" lineTo script(
					"radius" lineTo "12",
					"center" lineTo script(
						"x" lineTo "13",
						"y" lineTo "14"))))

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.at(word("x"))!!
			.recursiveRootPathTo(path())
			.assertEqualTo(path("circle", "center", "x"))

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.at(word("x"))!!
			.recursiveRootPathTo(path("x"))
			.assertEqualTo(path("circle", "center", "x"))

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.at(word("x"))!!
			.recursiveRootPathTo(path("center"))
			.assertEqualTo(path("circle", "center"))

		circle
			.at(word("circle"))!!
			.at(word("center"))!!
			.at(word("x"))!!
			.recursiveRootPathTo(path("center", "x"))
			.assertEqualTo(path("circle", "center", "x"))
	}
}