package tomorrowtoday_javadraft
import testplugin.FunctionName
import testplugin.testplugincore

/**
 * @author ${user.name}
 */
object App {


  def main(args : Array[String]) {
    println( "Hello World!" )
    val core = new Core()
    val plugin = new testplugincore(core)
    plugin.start()
    core.plugin = plugin
    core.start()
      sys.addShutdownHook({
          println("ShutdownHook called")
      })
      while (true) {}
  }



}
