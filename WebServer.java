import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class WebServer {
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(8080);
    while (true) {
      Socket clientSocket = serverSocket.accept();
      InputStream is = clientSocket.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      OutputStream os = clientSocket.getOutputStream();
      PrintWriter pw = new PrintWriter(os, true);

      String inString, fileName = "";
      while ((inString = br.readLine()) != null) {
        if (inString.length() == 0)
          break;
        if (inString.substring(0,3).equals("GET")) {
          String[] split = inString.split("\\s");
          fileName = "www/" + split[1].substring(1);
          //System.out.println(fileName);
        }
        System.out.println(inString);
      }
      try {
        File myFile = new File(fileName);
        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-type: text/html");
        pw.println("Content-length: 124");
        pw.println();
        Scanner fr = new Scanner(myFile);
        while (fr.hasNextLine()) {
          String newLine = fr.nextLine();
          pw.println(newLine);
        }
        fr.close();
      } catch (Exception e) {
        pw.println("HTTP/1.1 404 Not Found");
        pw.println("Content-type: text/html");
        pw.println("Content-length: 126");
        pw.println();
        File errorCode = new File("www/404code.html");
        Scanner fr = new Scanner(errorCode);
        while (fr.hasNextLine()) {
          String newLine = fr.nextLine();
          pw.println(newLine);
        }
        fr.close();
        pw.close();
        os.close();
      }
    }
  }
}