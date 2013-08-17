package tomorrowtoday_javadraft

import scala.actors.Actor
import org.json4s._


/**
 * Created with IntelliJ IDEA.
 * User: joern
 * Date: 8/12/13
 * Time: 9:51 AM
 * To change this template use File | Settings | File Templates.
 */
case class NewRequest(Request:JValue, peer: String)

trait PluginTrait extends Actor{
    implicit val formats = DefaultFormats

    def doStuff(request:JValue, peer: String)

    def recvStuff(reply:JValue, peer: String)

    def act(){
        loop{
            react{
                case NewRequest(req,p) => doStuff(req,p)
                case NewData(p,rep) => recvStuff(rep,p)
            }
        }
    }
}
