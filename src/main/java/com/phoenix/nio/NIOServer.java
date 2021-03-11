package com.phoenix.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOServer extends Thread{
    @Override
    public void run() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocket = ServerSocketChannel.open();) {
            serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
            //设置非阻塞
            serverSocket.configureBlocking(false);
            //注册到selector
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                //阻塞等待就绪
                selector.select();
                Set<SelectionKey> selectedKey = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKey.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    //生产系统中一般会将额外进行就绪状态检测
                    sayHelloWorld((ServerSocketChannel) key.channel());
                    iter.remove();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sayHelloWorld(ServerSocketChannel serverSocket) throws IOException {
        try (SocketChannel client = serverSocket.accept();) {
            client.write(Charset.defaultCharset().encode("Hello World!"));
        }
    }
}
