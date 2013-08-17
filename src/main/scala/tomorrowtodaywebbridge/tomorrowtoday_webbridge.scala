package tomorrowtodaywebbridge

/**
 * Created with IntelliJ IDEA.
 * User: joern
 * Date: 8/12/13
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */
import tomorrowtoday_javadraft.PluginTrait
import io.backchat.hookup._

class tomorrowtoday_webbridge{
    val self = this
    private final val wsserver = HookupServer(8888){
        new HookupServerClient{
            def receive = {
                case TextMessage(text)=>
            }
        }
    }

    def doStuff(Request:String){

    }

    var AvailableApiKeys: Array[String] = _
}
