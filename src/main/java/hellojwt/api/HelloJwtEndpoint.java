package hellojwt.api;

import akka.javasdk.annotations.Acl;
import akka.javasdk.annotations.JWT;

import akka.javasdk.annotations.http.Get;
import akka.javasdk.annotations.http.HttpEndpoint;
import akka.javasdk.http.AbstractHttpEndpoint;

// Opened up for access from the public internet to make the sample service easy to try out.
// For actual services meant for production this must be carefully considered and often set more limited
@Acl(allow = @Acl.Matcher(principal = Acl.Principal.INTERNET))
@HttpEndpoint("/hello")
@JWT(validate = JWT.JwtMethodMode.BEARER_TOKEN, bearerTokenIssuers = "my-issuer") // <1>
public class HelloJwtEndpoint extends AbstractHttpEndpoint {

  @Get("/")
  public String hello() {
    return "Hello, World!";
  }

  @JWT(
    validate = JWT.JwtMethodMode.BEARER_TOKEN,
    bearerTokenIssuers = { "my-issuer", "my-issuer2" },
    staticClaims = @JWT.StaticClaim(claim = "sub", values = "my-subject")
  )
  @Get("/claims")
  public String helloClaims() {
    var claims = requestContext().getJwtClaims(); // <1>
    var issuer = claims.issuer().get(); // <2>
    var sub = claims.subject().get(); // <2>
    return "issuer: " + issuer + ", subject: " + sub;
  }
}
