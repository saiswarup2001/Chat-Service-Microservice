package com.acejobber.chatserver;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.acejobber.chatserver.Entity.Conversation;
import com.acejobber.chatserver.Entity.Message;
import com.acejobber.chatserver.Entity.MessageInbox;
import com.acejobber.chatserver.MainService.ChatEntity;

@SpringBootApplication
public class ChatserverApplication {

	public static void main(String[] args) {

		SpringApplication.run(ChatserverApplication.class, args);

	}

	@Bean
	public SessionFactory getSession() {
		Configuration configuration = new Configuration();
		configuration.configure("hibernate.cfg.xml").addAnnotatedClass(Message.class);
		configuration.configure("hibernate.cfg.xml").addAnnotatedClass(MessageInbox.class);
		configuration.configure("hibernate.cfg.xml").addAnnotatedClass(Conversation.class);
		configuration.configure("hibernate.cfg.xml").addAnnotatedClass(ChatEntity.class);
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		System.out.println(session.isConnected());
		// return session;
		return sessionFactory;
	}
}
