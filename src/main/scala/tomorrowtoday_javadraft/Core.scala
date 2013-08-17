package tomorrowtoday_javadraft

import  scala.actors.Actor
import org.json4s._


case class KeyActive(key: ApiKey)
case class KeyInactive(key: ApiKey)
case class NewMsg(peer: String, data: JValue)
case class NewReq(peer: String, data: JValue)
case class NewRep(peer: String, data: JObject)
case class NewData(peer: String, data: JValue)

class Core() extends Actor{
    private val conf = new ConfigSupplier
    final val comlayer = new ComLayer(this)
    private final val ip = "192.168.1.123"
    var plugin: PluginTrait = null

    val ImportedApiKeys = conf.GetImportedApis()
    val ExportedApiKeys = conf.GetExportedApis()

    def act() {
        println( "Core Started!" )
        print("Implemented ApiKeys: ")
        conf.GetImportedApiNames().foreach(k => print(k+", "))
        println("")
        comlayer.start()

        ImportedApiKeys.foreach(k => comlayer ! InitApiKeyImport(k))
        ExportedApiKeys.foreach(k => comlayer ! InitApiKeyExport(k))
        //plugin.init()
        //val brocaster = new Brocaster(uid, incoming_port, this)
        //brocaster.start()
        loop {
            react {
                case KeyActive(k) => {
                    println("Juhu, key activated: "+k.name)

                }

                case KeyInactive(k) => {
                    println("Ohh snap, key deactivated: "+k.name)

                }

                case NewMsg(peer, data) => {
                    println("Got Msg from Peer: "+peer+", contaning: "+data)
                    plugin ! NewRequest(data,peer)

                }

                case NewReq(keyname, req) => {
                    println("Sending request "+req)
                    comlayer ! SendReq(keyname,req)
                }
                case NewRep(keyname, rep) => {
                    println("Sending reply "+rep)
                    comlayer ! SendRep(keyname,rep)
                }
                case NewData(peer, rep) => {
                    println("Got reply "+rep)
                    plugin ! NewData(peer,rep)
                }
            }
        }
    }




}
