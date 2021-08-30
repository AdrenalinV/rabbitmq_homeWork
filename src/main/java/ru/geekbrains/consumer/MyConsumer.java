package ru.geekbrains.consumer;

import com.rabbitmq.client.*;

import java.util.Scanner;


public class MyConsumer {
    private static final String EXCHANGE_NAME = "sending_notes";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection con = factory.newConnection();
        Channel channel = con.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();
        Scanner in=new Scanner(System.in);
        String rawKey;
        do{
            System.out.println("input set_topic xxxxx, xxxxx - topic name");
            rawKey=in.nextLine();
        }while(!rawKey.startsWith("set_topic"));
        String routingKey = rawKey.split(" ",2)[1];
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        System.out.println("Received key= " + routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received msg=" + msg);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}

