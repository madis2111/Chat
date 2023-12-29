package chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Server {
    private ServerSocket serverSocket;
    private BufferedWriter logger;
    private Map clients;

    public Server() {

        try {
            logger = new BufferedWriter(new FileWriter("src/main/resources/serverFiles/file.log"));
            clients = new HashMap<String, BufferedWriter>();

            String port = new BufferedReader(new FileReader("src/main/resources/serverFiles/settings.txt")).readLine();
            serverSocket = new ServerSocket(Integer.parseInt(port));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start() {

        while (true) {
            Socket socket = serverSocket.accept();

            new Thread(() -> {
                try (InputStream inputStream = socket.getInputStream();
                     InputStreamReader isr = new InputStreamReader(inputStream);
                     BufferedReader reader = new BufferedReader(isr);
                     OutputStream outputStream = socket.getOutputStream();
                     OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                     BufferedWriter writer = new BufferedWriter(osw)) {

                    writer.write("Добро пожаловать в чат");
                    writer.newLine();
                    writer.flush();

                    writer.write("Введите имя для участия в чате:");
                    writer.newLine();
                    writer.flush();

                    String clientName = reader.readLine();
                    clients.put(clientName, writer);

                    while (true) {
                        System.out.println("inside while");
                        //          Сергей привет
                        String line = clientName + ": " + reader.readLine();
                        String time = LocalDateTime.now() + " ";

                        Set keys = clients.keySet();
                        for (Object nameFromMap : keys) {
                            if (clientName.equals(nameFromMap)) {
                                continue;
                            }
                            BufferedWriter bw = (BufferedWriter)(clients.get(nameFromMap));
                            bw.write(line);
                            bw.newLine();
                            bw.flush();
                        }

                        line = time + line;

                        logger.write(line);
                        logger.write("\n");
                        logger.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}