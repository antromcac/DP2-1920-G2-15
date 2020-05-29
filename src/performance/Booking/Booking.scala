package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Booking extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".css""", """.js""", """.ico""", """.png""", """.*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es,en-US;q=0.9,en;q=0.8")
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

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(17).
		exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "vet1")
			.formParam("password", "v3t")
			.formParam("_csrf", "${stoken}"))
		.pause(17)
	}

	object ListBookings{
		val listbookings = exec(http("List booking")
			.get("/bookings/list")
			.headers(headers_0))
		.pause(15)
	} 

	object CreateBooking {
		val createBooking = exec(http("Create Form")
			.get("/bookings/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(23)
		.exec(http("Create Post")
			.post("/bookings/new")
			.headers(headers_3)
			.formParam("fecha", "2020-05-24")
			.formParam("petId", "1")
			.formParam("vetId", "1")
			.formParam("roomId", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(19) 
	}

	object UpdateBooking {
		val updateBooking = exec(http("Update Form")
			.get("/bookings/edit/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(27)
		.exec(http("Update Post")
			.post("/bookings/edit/1")
			.headers(headers_3)
			.formParam("fecha", "2020-01-05")
			.formParam("petId", "6")
			.formParam("vetId", "3")
			.formParam("roomId", "2")
			.formParam("_csrf", "${stoken}"))
		.pause(5)
	}

	object DeleteBooking {
		val deleteBooking = exec(http("delete")
			.get("/bookings/2/delete")
			.headers(headers_0))
		.pause(6)
	}

	val listScn = scenario("List").exec(Home.home,
									Login.login,
									ListBookings.listbookings)

	val createScn = scenario("Create").exec(Home.home,
									Login.login,
									ListBookings.listbookings,
									CreateBooking.createBooking)
						
	val updateScn = scenario("Update").exec(Home.home,
									Login.login,
									ListBookings.listbookings,
									UpdateBooking.updateBooking)

	val deleteScn = scenario("Delete").exec(Home.home,
									Login.login,
									ListBookings.listbookings,
									DeleteBooking.deleteBooking)
		
		

	setUp(
		listScn.inject(rampUsers(300) during (10 seconds)),
		createScn.inject(rampUsers(300) during (10 seconds)),
		updateScn.inject(rampUsers(300) during (10 seconds)),
		deleteScn.inject(rampUsers(300) during (10 seconds)),
		).protocols(httpProtocol)
}