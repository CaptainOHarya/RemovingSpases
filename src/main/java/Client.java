import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private final static int port = 44443;


    public static void main(String[] args) throws IOException {
        // Определяем сокет сервера
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", port);
        final SocketChannel socketChannel = SocketChannel.open();

        // подключаемся к серверу
        socketChannel.connect(socketAddress);

        // получаем входящий и исходящий потоки информации
        try (Scanner scanner = new Scanner(System.in)) {

            // Определяем буффер для полуения данных
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true) {
                System.out.println("Наберите любое сообщение для сервера или 'end', если хотите завершить работу");
                msg = scanner.nextLine();
                // завершим работу, если пользователь введёт "end"
                if ("end".equals(msg)) break;

                socketChannel.write(
                        ByteBuffer.wrap(
                                msg.getBytes(StandardCharsets.UTF_8)));

                Thread.sleep(2000);

                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount,
                        StandardCharsets.UTF_8).trim());
                inputBuffer.clear();

            }
            scanner.close();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            socketChannel.close();

        }

    }

}
