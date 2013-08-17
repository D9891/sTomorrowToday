package tomorrowtoday_javadraft

import org.zyre
import scala.actors.Actor
import org.zyre.{ZreLog, ZreLogger, ZreInterface}
import org.jeromq.{ZLogManager, ZLog, ZMsg, ZFrame}
import org.jeromq.ZMQ.Poller
import tomorrowtoday_javadraft.ApiKey
import scala.collection.mutable.ListBuffer
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.specs2.mutable.Specification


/**
 * Created with IntelliJ IDEA.
 * User: joern
 * Date: 8/16/13
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */
case class NewPeer(peer: String)
case class PeerGone(peer: String)

case class SendReq(key: String, data: JValue)
case class SendRep(key: String, data: JValue)
case class InitApiKeyImport(ApiKey: ApiKey)
case class InitApiKeyExport(ApiKey: ApiKey)
case class Exports(exports:List[String])

class ComLayer(core: Actor) extends Actor{
    private final val iface = new ZreInterface()
    private final val incoming = new incoming()
    private var iKeys: ListBuffer[ApiKey] = ListBuffer[ApiKey]()
    private var oKeys: ListBuffer[ApiKey] = ListBuffer[ApiKey]()
    val self = this
    incoming.start()


    override def act(){
        loop{
            react{
                case SendReq(key,data) => {

                    iKeys.foreach({
                        k => if (k.name == key){
                            if (k.hasImplementor()) {
                                val msg = new ZMsg
                                msg.add(k.getImplementor())
                                msg.add(compact(render(data)))
                                iface.whisper(msg)
                            }
                        }
                    })
                }

                case SendRep(key, data) => {
                    val msg = new ZMsg
                    msg.add(key)
                    msg.add(compact(render(data)))
                    iface.shout(msg)
                }
                case PeerGone(peer) => {
                    iKeys.foreach(k =>{
                        k.removeImplementor(peer)
                        if (!k.hasImplementor()) core ! KeyInactive(k)
                    })
                }
                case NewPeer(peer) => {
                    var exports = ListBuffer[String]()
                    var i =0
                    oKeys.foreach(k=>exports+=k.name)
                    val json =
                        (("apikey" -> "AHOI")~("exports" -> exports))
                    val msg = new ZMsg
                    msg.add(peer)
                    msg.add(compact(render(json)))
                    iface.whisper(msg)
                }
                case InitApiKeyImport(key) => {
                    iKeys.append(key)
                    iface.join(key.name)
                    println("Initiated Key Import for "+key.name)
                }
                case InitApiKeyExport(key) => {
                    oKeys.append(key)
                    println("Initiated Key Export for: "+key.name)
                }
            }
        }
    }


    private class incoming extends Actor{
        override def act{
            while (true){
                //println("listening")
                var incoming = iface.recv()
                var cmd = incoming.popString()
                //println(cmd)
                var peer = incoming.popString()
                //println(peer)

                cmd match {
                    case "ENTER" => self ! NewPeer(peer)
                    case "EXIT" => self ! PeerGone(peer)
                    case "SHOUT" => {
                        incoming.popString()
                        core ! NewData(peer, parse(incoming.popString()))
                    }
                    case "WHISPER" => {
                        val req = parse(incoming.popString())
                        val reqKey = compact(render(req \ "apikey"))

                        //Check if some body want to great us
                        if (reqKey == "\"AHOI\""){
                            var pKeys = ListBuffer[String]()
                            val exports = req \"exports" \\classOf[JArray]    // .extract[Exports] must_==Exports(List[String]())
                            exports.foreach(l => l.foreach(e => pKeys+=e.toString()))
                            iKeys.foreach(k => {
                               pKeys.foreach(p => {
                                   if (p==k.name) {
                                       if (!k.hasImplementor()) core ! KeyActive(k)
                                       k.addImplementor(peer)
                                   }
                               })
                            })
                        } else {
                            core ! NewMsg(peer,req)
                        }



                    }
                    case _ => println("KA")
                }
                //if (cmd.equals("SHOUT")||cmd.equals("WHISPER")) core ! NewMsg(peer, incoming)

            }
        }
    }

    private class loggeractor extends Actor{
        override def act{
            while (true){
                println("logging")

                //if (cmd.equals("SHOUT")||cmd.equals("WHISPER")) core ! NewMsg(peer, incoming)

            }
        }
    }
}
