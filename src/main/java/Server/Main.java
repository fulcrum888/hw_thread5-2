package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

//Для этой задачи можно использовать Non-Blocking IO, так как время ввода
//текста пользователем намного больше, чем время обработки данных
//и работа клиента никак не зависит от работы сервера, так что нет необходимости ждать
public class Main {
    public static void main(String[] args) {
        try {
            final ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("127.0.0.1", 20888));

            while (true) {
                SocketChannel socketChannel = serverChannel.accept();
                final ByteBuffer inputBuffer = ByteBuffer.allocate(1024);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);

                    if (bytesCount == -1) break;

                    final String msg = new String(inputBuffer.array(), 0, bytesCount,
                            StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    socketChannel.write(ByteBuffer.wrap(msg.replaceAll("\\s+", "")
                            .getBytes(StandardCharsets.UTF_8)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
