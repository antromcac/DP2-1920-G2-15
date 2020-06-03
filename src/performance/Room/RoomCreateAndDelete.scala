package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RoomCreateAndDelete extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8,fr;q=0.7")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home{
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0)
		).pause(5)
	}
	object Login{
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken1"))
		).pause(19)
		.exec(
			http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken1}")
		).pause(14)
	}

	object RoomList{
		val roomList = exec(http("RoomList")
			.get("/rooms/roomsList")
			.headers(headers_0))
		.pause(13)
	}

	object DeleteRoom{
		val deleteRoom = exec(http("DeleteRoom")
					.get("/rooms/delete/6")
					.headers(headers_0))
					.pause(12)
	}

	object CreateRoom{
		val createRoom = exec(http("CreateRoomForm")
			.get("/rooms/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken2")))
		.pause(27)
		.exec(http("CreatedRoom")
			.post("/rooms/new")
			.headers(headers_3)
			.formParam("name", "room")
			.formParam("floor", "1")
			.formParam("medicalTeam", "bisturi")
			.formParam("_csrf", "${stoken2}"))
		.pause(17)
	}

	val createRoomScn = scenario("CreateRoom").exec(Home.home,
													Login.login,
													RoomList.roomList,
													CreateRoom.createRoom)

	val deleteRoomScn = scenario("DeleteRoom").exec(Home.home,
													Login.login,
													RoomList.roomList,
													DeleteRoom.deleteRoom)
		

	setUp(
		createRoomScn.inject(rampUsers(300) during (100 seconds)),
		deleteRoomScn.inject(rampUsers(300) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
		forAll.failedRequests.percent.lte(5),
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(5000),
		global.successfulRequests.percent.gt(95)
	)
}