package chat;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private InputStream inputStream;
    private InputStreamReader reader;
    private BufferedReader bufferedReader;
    private BufferedWriter logger;

    private OutputStream outputStream;
    private OutputStreamWriter writer;
    private BufferedWriter bufferedWriter;

    public Client() {


        try {

            BufferedReader settingsReader = new BufferedReader(new FileReader("src/main/resources/clientFiles/settings.txt"));
            String host = settingsReader.readLine();
            int port = Integer.parseInt(settingsReader.readLine());

            socket = new Socket(host, port);

            inputStream = socket.getInputStream();
            reader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(reader);

            outputStream = socket.getOutputStream();
            writer = new OutputStreamWriter(outputStream);
            bufferedWriter = new BufferedWriter(writer);

            Scanner s = new Scanner(System.in);

            String msgFromServer = readMsg();
            System.out.println(msgFromServer);      // Добро пожаловать
            System.out.println("Введите exit для завершения");

            msgFromServer = readMsg();
            System.out.println(msgFromServer);      // Введите имя

            String name = s.nextLine();         // todo send name to serve
            sendMsg(name);

            logger = new BufferedWriter(new FileWriter("src/main/resources/clientFiles/" + name  + "File.log"));




            new Thread(() -> {
                while (true) {
                    String line = readMsg();
                    String time = LocalDateTime.now() + "   ";

                    System.out.println(line);
                                                    // todo print time and message to chat.log

//                    try {
//                        line = name + ": " + bufferedReader.readLine();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }

                    line = time + "   " + line;

                    try {
                        logger.write(line);
                        logger.write("\n");
                        logger.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            while (true) {
                String vvod = s.nextLine();

                String line = name + ": " + vvod;
                String time = LocalDateTime.now() + "   ";

                line = time + line;

                try {
                    logger.write(line);
                    logger.write("\n");
                    logger.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (vvod.equals("/exit")) {
                    break;
                }
                sendMsg(vvod);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(String msg) {
        try {
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readMsg() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        try (Socket socket = new Socket("localhost", 8080)) {
//            bufferedWriter.write("Hello");
//        } catch (UnknownHostException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
