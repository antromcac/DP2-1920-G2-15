package petclinicRoom

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TestPerformanceUpdateAndShow extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8, application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-419,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

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
		val home = exec(
			http("Home")
			.get("/")
			.headers(headers_0)
		).pause(5)
	}
	object Login{
		val login = exec(
			http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(18)
		.exec(
			http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}")
		).pause(12)
	}

	object RoomsList{
		val roomsList = exec(
			http("RoomsList")
			.get("/rooms/roomsList")
			.headers(headers_0))
		.pause(13)
	}
	object UpdateRoom{
		val updateRoom = exec(
			http("RoomUpdateForm")
			.get("/rooms/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(27)
		.exec(http("RoomUpdated")
			.post("/rooms/1/edit")
			.headers(headers_3)
			.formParam("name", "updated")
			.formParam("floor", "1")
			.formParam("medicalTeam", "nadena")
			.formParam("_csrf", "${stoken}")
		).pause(14)
	}
	object ShowRoom{
		val showRoom = exec(http("ShowRoom")
			.get("/rooms/1")
			.headers(headers_0))
		.pause(11)
	}
	val updateRoomScn = scenario("RoomsUpdate").exec(Home.home,
									  Login.login,
									  RoomsList.roomsList,
									  UpdateRoom.updateRoom)
	val showRoomScn = scenario("RoomsShow").exec(Home.home,
									  Login.login,
									  RoomsList.roomsList,
									  ShowRoom.showRoom)
	setUp(
		updateRoomScn.inject(rampUsers(1000) during (100 seconds)),
		showRoomScn.inject(rampUsers(1000) during (100 seconds))
		).protocols(httpProtocol)
     	.assertions(
			forAll.failedRequests.percent.lte(5),
        	global.responseTime.max.lt(50000),    
        	global.responseTime.mean.lt(1200),
        	global.successfulRequests.percent.gt(95)
     )
}
