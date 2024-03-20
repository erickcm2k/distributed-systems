/*
  ServidorHTTP.java
  Carlos Pineda G. 2024
*/

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;

class ServidorHTTP {
  static class Worker extends Thread {
    Socket conexion;

    Worker(Socket conexion) {
      this.conexion = conexion;
    }

    public void run() {
      try {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        PrintWriter salida = new PrintWriter(conexion.getOutputStream());

        String req = entrada.readLine();

        System.out.println("req: " + req);

        Map<String, String> queryParams = new HashMap<>();
        String[] reqParts = req.split(" ");
        if (reqParts.length >= 2 && reqParts[1].contains("?")) {
          String[] pathAndQuery = reqParts[1].split("\\?");
          String queryString = pathAndQuery[1];
          String[] params = queryString.split("&");
          for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
              String key = URLDecoder.decode(keyValue[0], "UTF-8");
              String value = URLDecoder.decode(keyValue[1], "UTF-8");
              queryParams.put(key, value);
            }
          }
        }
        int kInicial = Integer.parseInt(queryParams.get("par1"));
        int KFinal = Integer.parseInt(queryParams.get("par2"));

        for (;;) {
          String encabezado = entrada.readLine();
          if (encabezado.equals(""))
            break;
        }

        if (req.startsWith("GET /pi")) {

          BigDecimal resultado = RamanujanSumRequest.executeProgram(kInicial, KFinal);

          String respuesta = "<body style=\"display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0;\">\n"
              +
              "  <div style=\"text-align: center; max-width: 90%;\">\n" +
              "    <textarea>\"" + resultado + "\"</textarea>\n" +
              "  </div>\n" +
              "</body>";

          salida.println("HTTP/1.1 200 OK");
          salida.println("Content-type: text/html; charset=utf-8");
          salida.println("Content-length: " + respuesta.length());
          salida.println("Connection: close");
          salida.println();
          salida.println(respuesta);
          salida.flush();
        } else {
          salida.println("HTTP/1.1 404 File Not Found");
          salida.flush();
        }
      } catch (Exception e) {
        System.err.println(e.getMessage());
      } finally {
        try {
          conexion.close();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
    ServerSocket servidor = new ServerSocket(4000);
    System.out.println("Servidor ejecutandose en puerto " + 4000);
    for (;;) {
      Socket conexion = servidor.accept();
      new Worker(conexion).start();
    }
  }
}