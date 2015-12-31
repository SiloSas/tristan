import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

import administration.UserActor

class DTEVModule extends AbstractModule with AkkaGuiceSupport {
  def configure() = {
    bindActor[UserActor]("user-actor")
  }
}