package tomorrowtoday_javadraft
import testplugin.FunctionName
import testplugin.testplugincore

/**
 * @author ${user.name}
 */
object App {


  def main(args : Array[String]) {
    val core = new Core()
    val plugin = new testplugincore(core)
    plugin.start()
    core.plugin = plugin
    core.start()
  }



}
