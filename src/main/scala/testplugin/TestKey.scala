package testplugin

import tomorrowtoday_javadraft.ApiKey
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.specs2.mutable.Specification
/**
 * Created with IntelliJ IDEA.
 * User: joern
 * Date: 8/17/13
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */

class TestKey() extends ApiKey("{\"apikey\":\"TestApi\",\"functions\":[{\"name\":\"echo\",\"inputs\":[{\"name\":\"msg\",\"datatype\":\"string\"}],\"outputs\":[{\"name\":\"echo\",\"datatype\":\"string\"}]}]}"
) {
    def echo(msg: String)(){
        val req = (
            ("apikey" -> "TestKey")~
                ("function" -> "echo")~
                ("msg" -> msg)
            )
        send(req)
    }
}
