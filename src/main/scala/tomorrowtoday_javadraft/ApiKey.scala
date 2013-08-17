package tomorrowtoday_javadraft

import scala.collection.mutable.ListBuffer
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scala.actors.Actor

/**
 * Created with IntelliJ IDEA.
 * User: joern
 * Date: 8/17/13
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
class ApiKey(KeyString: String){
    implicit val formats = DefaultFormats
    private var implementors = ListBuffer[String]()
    private var core: Actor = null
    println(parse(KeyString).extract[Key])

    def name: String = "TestKey"

    def setCore(Core: Actor) {core=Core}

    def getCore: Actor = core

    def hasImplementor(): Boolean = !implementors.isEmpty

    def addImplementor(peer: String) {implementors.append(peer)}

    def removeImplementor(peer: String){
        if (implementors.contains(peer)){
            var i: Int = 0
            var index = 0
            implementors.foreach(imp => {if (imp==peer) index=i; i+=1})
            implementors.remove(index)
        }
    }

    def getImplementor():String = {
        if (implementors.length != 0) return implementors(0)
        else return ""
    }

    def send(req: JObject){
        core ! NewReq(name, req)
    }

}

    case class Key(apikey:String, functions: List[fkt])
    case class fkt(name: String, inputs: List[input], outputs: List[output])
    case class input(name: String, datatype: String)
    case class output(name: String, datatype: String)


