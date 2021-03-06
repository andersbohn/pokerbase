package client

import java.nio.file._
import scala.collection.JavaConversions._
import java.io.{BufferedReader, IOException}
import java.util.{UUID, Date}
import java.nio.charset.Charset
import java.text.SimpleDateFormat

object Harvester extends App {

  val MAXLINES = 200

  val charset = Charset.forName("UTF-8")

  // TODO this should be configurable for the client - auth mechanism as well, integrated with other parts (HUD/Mobile/Browser)
  val username = "abj"

//  val folder = System.getProperty("handHistoryFolder", "/Users/abj/Library/Application Support/PokerStarsDK/HandHistory/aazaa/")
  val folder = System.getProperty("handHistoryFolder", "/Users/abj/Library/Application Support/PokerStars/HandHistory/aazaa/")
//    val folder = System.getProperty("handHistoryFolder", "/Users/abj/Projects/pokerbase/test/harvester/files")

  // TODO design: write an index-file om some sort ? avoid moving files, if possible, just read 'em all and then watch changes
  // TODO design: add replication style upload, eg storing last uploaded line-number (+ maybe a CSC) to upload only the appended handhistories since last upload

  var fileStatusMap: Map[Path, FileStatus] = Map()

  def loadPath() = {

    val path = Paths.get(folder)

    val directoryStream = Files.newDirectoryStream(path)
    directoryStream.foreach {
      path =>
        println("load " + path)
        load(path)
    }

  }

  def run() = {

    val path = Paths.get(folder)

    val watcher: WatchService = path.getFileSystem.newWatchService

    path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)

    while (true) {
      val watckKey: WatchKey = watcher.take()

      watckKey.pollEvents.map {
        event: WatchEvent[_] =>
          event.kind() match {
            case StandardWatchEventKinds.ENTRY_CREATE =>
              println("Created: " + event.context)

              try {
                val filename: Path = event.asInstanceOf[WatchEvent[Path]].context

                load(filename)
              } catch {
                case e: IOException =>
                  println("error reading file " + e)
              }

            case StandardWatchEventKinds.ENTRY_DELETE =>
              println("Delete: " + event.context)
            case StandardWatchEventKinds.ENTRY_MODIFY =>
              println("Modify: " + event.context)

              try {
                val filename: Path = event.asInstanceOf[WatchEvent[Path]].context

                reload(filename)
              } catch {
                case e: IOException =>
                  println("error reading file " + e)
              }

          }

          ""
      }

    }
  }

  def reload(file: Path) = {
    fileStatusMap.get(file).map {
      fileStatus =>
        doLoad(file, fileStatus.lines.getOrElse(0))
    }.getOrElse(doLoad(file, 0))
  }


  def load(file: Path) = {
    doLoad(file, 0)
  }

  def doLoad(file: Path, startAtLine: Int) = {

    val reader: BufferedReader = Files.newBufferedReader(file, charset)

    var linesRead = 0
    var reading = true
    var started = false
    var currentBlock = new StringBuilder
    while (reading) {
      val line = reader.readLine
      reading = line != null
      if (reading) {
        linesRead += 1
        if (linesRead > startAtLine) {
          if (!started) {
            if (startAtLine == 0 && (linesRead == 1 || line.trim == "")) {
              started = true
            } else {
              println("unexpected place to start... '" + line + "' @ " + linesRead)
            }
          }
          if (started) {
            if (line.trim == "" && currentBlock.length > 0) {
              if (currentBlock.toString().trim.length > 0 && linesRead < MAXLINES) {
                putHandHistoryBlock(currentBlock)
              }
              currentBlock.clear()
            }
          }
          currentBlock.append(line)
          currentBlock.append("\n")
        }
      }
    }

    fileStatusMap += file -> Ok(new Date, linesRead)
  }

  val timeFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss")


  def putHandHistoryBlock(currentBlock: StringBuilder) {

    import scala.concurrent.ExecutionContext.Implicits.global

    println("PUT: " + currentBlock.substring(0, math.min(currentBlock.length, 20)))
/* //FIXME replace with akka http ...
    val thost = host("localhost", 9000)
    val request = thost / "handhistory"

    val basicAuth = Helper.encodeBasicAuth(username, "xxx") // TODO insert real auth stuff
    val putRequest = request
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", basicAuth)
        .setMethod("PUT")
    val raw = currentBlock.toString.replaceAll("\n", "\\\\n")
    val uuid = UUID.randomUUID().toString
    val timestamp = new Date
    val body = s"""{"id":"$uuid","owner":"$username","source":"harvester","timestamp":"${timeFormat.format(timestamp)} +0100","raw":"$raw"}"""
    val postRequest = putRequest << body

    val response = Http(postRequest OK as.String)

    response.onComplete {
      status =>
        println("PUT? " + status)

    }
*/
    // Have som async followup on status ?
  }

  loadPath()

  //  run()

}
