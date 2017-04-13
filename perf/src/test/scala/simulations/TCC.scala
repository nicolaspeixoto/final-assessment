import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class TCC extends Simulation {

  object Steps {

    val request = forever(exec(
      http("Post File").post("/")
        .header("Content-Type", "multipart/form-data")
        .bodyPart(RawFileBodyPart("data", "fileupload.pdf"))
        .check(bodyString.saveAs("id"))
    ).pause(1, 3)
    .exec(
      http("Get File")
        .get("/${id}")
    )
    .pause(1,3)
    .exec(
      http("PUT File")
        .put("/${id}")
        .header("Content-Type", "multipart/form-data")
        .bodyPart(RawFileBodyPart("data", "fileupload.pdf"))
    )
    .pause(1,3)
    .exec(
      http("DELETE File")
        .delete("/${id}")
        .header("Content-Type", "multipart/form-data")
    )
    .pause(1,3)
    )
  }

  var baseUrl = System.getenv("BASE_URL")

  if(baseUrl == null){
    baseUrl = "http://127.0.0.1:3000"
  }

  val httpProtocol = http
    .maxConnectionsPerHostLikeFirefox
    .disableWarmUp.baseURL(baseUrl)
    .inferHtmlResources()
    .extraInfoExtractor(extraInfo => List(extraInfo.response.body))

  val scn = scenario("TCC").exec(Steps.request)

  setUp(scn.inject(constantUsersPerSec(64) during(60 seconds))).protocols(httpProtocol)
    .maxDuration(1 minutes)

}