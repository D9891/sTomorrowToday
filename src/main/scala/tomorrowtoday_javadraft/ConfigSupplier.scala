package tomorrowtoday_javadraft

import scala.reflect.io.File
import scala.collection.mutable.ListBuffer
import scala.collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: joern
 * Date: 8/16/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */


class ConfigSupplier {

    private final val testKey = "{\"apikey\":\"TestApi\",\"functions\":[{\"name\":\"echo\",\"inputs\":[{\"name\":\"msg\",\"datatype\":\"string\"}],\"outputs\":[{\"name\":\"echo\",\"datatype\":\"string\"}]}]}"


    private var configfile: File = null
    private var devmode = false
    private var ImportedApiKeys: List[ApiKey] = List(new ApiKey(testKey))
    private var ExportedApiKeys: List[ApiKey] = List(new ApiKey(testKey))
    def ConfigSupplier(){
        devmode = true
    }

    def GetImportedApis() : ListBuffer[ApiKey] = {
       var allkeys = ListBuffer[ApiKey]()
       allkeys.append(new ApiKey(testKey))
       return allkeys
    }

    def GetExportedApis() : ListBuffer[ApiKey] = {
        var allkeys = ListBuffer[ApiKey]()
        allkeys.append(new ApiKey(testKey))
       return allkeys
    }

    def GetImportedApiNames() : ListBuffer[String] = {
        var allnames = ListBuffer[String]()
        ImportedApiKeys.foreach(k => {
            allnames.append(k.name)
        })
        return allnames
    }


    def GetExportedApiNames() : ListBuffer[String] = {
        var allnames = ListBuffer[String]()
        ExportedApiKeys.foreach(k => {
            allnames.append(k.name)
        })
        return allnames
    }



}

