package testplugin

import tomorrowtoday_javadraft.{NewRep, ConfigSupplier, PluginTrait}
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scala.actors.Actor

//case class to parse name of fkt
case class FunctionName(function: String)

//Case classes for all functions  to process all req

//function echo
case class Echo(msg: String)
case class Recho(echo: String)

class testplugincore(core: Actor) extends PluginTrait{
    private final val plugin = new testplugin(core)
    private final val pluginmain = new pluginmain(core)
    pluginmain.start()

    def doStuff(request: JValue, peer: String) {
        val name = request.extract[FunctionName]

        println("doing stuff "+name)
        name match {
            case FunctionName("echo") => {
                val rep = (
                    ("apikey" -> "TestKey")~
                        ("function" -> "echo")~
                        ("echo" -> plugin.echo(request.extract[Echo].msg)))
                sender ! NewRep("TestKey", rep)
            }
        }
    }

    def recvStuff(reply: _root_.org.json4s.JValue, peer: String) {
        val name = reply.extract[FunctionName]
        name match {
            case FunctionName("echo") => {
                plugin.receiveEcho(reply.extract[Recho].echo)
            }
        }
    }
}

class pluginmain(core: Actor) extends Actor {
    private val plugin = new testplugin(core)
    override def act(){
        plugin.main()
    }
}

class plugin(core: Actor){

    def getTestKeyHandle: TestKey = {
        var k = new TestKey()
        k.setCore(core)
        return k
    }
}


class testplugin(core:Actor) extends plugin(core) {
    def echo(msg: String): String = {
        val echo: String = "Brofist! "+msg
        return echo
    }

    def receiveEcho(echo: String) {
        println(echo)
    }

    def main(){
        var key = getTestKeyHandle
        println("hi, hier plugin")
        while(true){
            key.echo("Ola brotha")
            Thread.sleep(1000)
        }
    }
}