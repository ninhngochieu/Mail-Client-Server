import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static Socket mainSocket;
    private static Socket mailListenerSocket;
    private static BufferedReader mailBufferedReader;
    private static BufferedWriter mailBufferedWriter;

    public static void main(String[] args) throws IOException {
        mainSocket = new Socket(HOST,PORT);
        bufferedReader = new BufferedReader(new InputStreamReader(mainSocket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(mainSocket.getOutputStream()));
//
//        mailListenerSocket =  new Socket(HOST, PORT);
//        mailBufferedReader = new BufferedReader(new InputStreamReader(mailListenerSocket.getInputStream()));
//        mailBufferedWriter = new BufferedWriter(new OutputStreamWriter(mailListenerSocket.getOutputStream()));

        Scanner scanner = new Scanner(System.in);
        Thread mainThead = new Thread(() -> {
            while (true){
                String data = scanner.nextLine();
                sendDataToServer(data, bufferedWriter);
                receiveDataFromServer(bufferedReader);
            }
        });
        Thread mailListener = new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String data = "Listening Data";
                sendDataToServer(data, bufferedWriter);
                receiveDataFromServer(bufferedReader);
            }
        });

        mainThead.start();
        mailListener.start();

    }

    private static void receiveDataFromServer(BufferedReader Reader) {
        try {
            String data = Reader.readLine();
            System.out.println("Client receive: "+ data);
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    private static void sendDataToServer(String data, BufferedWriter Writer) {
        try {
            Writer.write(data);
            Writer.newLine();
            Writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    private static void closeConnection() {
        try {
            bufferedWriter.close();
            bufferedReader.close();
            mainSocket.close();

            mailBufferedReader.close();
            mailBufferedWriter.close();
            mailListenerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
