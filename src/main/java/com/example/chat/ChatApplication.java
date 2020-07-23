package com.example.chat;

import com.example.chat.config.GroupChatServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        new GroupChatServer(80).run();
    }
}
