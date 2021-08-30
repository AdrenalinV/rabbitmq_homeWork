package ru.geekbrains.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MyProducer {
    private static final String EXCHANGE_NAME = "sending_notes";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection con = factory.newConnection();
             Channel channel = con.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            Scanner in = new Scanner(System.in);
            while (true) {
                System.out.println("input send message or Q - exit");
                String rawString = in.nextLine();
                if (rawString.startsWith("Q")) {
                    break;
                }
                String[] msgraw=rawString.split(" ",2);
                String routingKey = msgraw[0];
                String message = msgraw[1];

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("Send Key=" + routingKey + " msg=" + message);
            }
        }
    }
}
