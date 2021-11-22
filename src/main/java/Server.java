import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    private final static int port = 44443;

    public static void main(String[] args) throws IOException {

        // занимаем порт, определяя серверный сокет
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", port));

        while (true) {
            // Ждём подключения клиента и получаем потоки для дальнейшей работы
            try (SocketChannel socketChannel = serverChannel.accept()) {
                // Определяем буфер для получения данных
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    // читаем данные из канала в буфер
                    int bytesCount = socketChannel.read(inputBuffer);

                    // если из потока нельзя читать, перестаём работать с этим клиентом
                    if (bytesCount == -1) break;

                    // получаем переданную от клиента строку в нужной кодировке и очищаем буфер
                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();

                    // удаляем пробелы
                    String answer = msg.replaceAll("\\s+", "");

                    //отправляем полученный результат назад клиенту
                    socketChannel.write(ByteBuffer.wrap(answer.getBytes(StandardCharsets.UTF_8)));
                }


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

    }

}
