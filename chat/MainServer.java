package chat;

public class MainServer {
    public static void main(String[] args) {

        new Server().start();
    }
}
//        try {



//            Client client1 = new Client("localhost", 8088);
//            Client client2 = new Client("localhost", 8088);

//            client1.sendMsg("hello");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        // while (true) {
//            String vkhodiashiyText = bufferedReader.readLine();
//            System.out.println(vkhodiashiyText);
        // }