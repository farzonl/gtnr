package com.gtnightrover.networking;


import java.util.Arrays;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jpxx.mail.MailServer;
import org.jpxx.mail.domain.DomainHandler;
import org.jpxx.mail.user.UserHandler;

/**
 * SendMail Implementation using simple SMTP.
 *
 * 
 */
public class Socket_Mail {

	private String smtpHost = "localhost";

	public void sendMessage(String from, String[] recipients, String subject, String message)
	throws MessagingException {
		boolean debug = false;

		//Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", "2500");

		// create some properties and get the default Session
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);


		// Setting the Subject and Content Type
		msg.setSubject(subject);
		
		msg.setContent(message, "text/html");
		Transport.send(msg);
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	
	public void startnsend(String from, String subj, String mesg, String[] recipients)
	{
		DomainHandler dc = new DomainHandler();
		dc.addDomain("127.0.0.1");
		UserHandler uh = new UserHandler();
		uh.addUser("system", "system", "127.0.0.1");
		System.out.println("Mail Server State: "+MailServer.getState());
		
		MailServer.getSingletonInstance();
		MailServer.getSingletonInstance().startServer();

		try {
			sendMessage(from, recipients, subj, mesg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sent");
		MailServer.getSingletonInstance().stopServer();
	}
	
	public static void caller(String args[]) 
	{
		String recipients[] = null;
		String subj = null, mesg = null, from = null;
		if(args.length > 3)
		{
			from = args[0];
			subj = args[1];
			mesg = args[2];
			recipients = Arrays.copyOfRange(args, 3,args.length);
		}
		else
		{
			System.out.println("The Default syntax is <from> <subject> <mesg> <recipient 0> ...<recipient n>");
			return;
		}
			
		Socket_Mail test = new Socket_Mail();
		test.startnsend(from, subj, mesg, recipients);
	}

	public static void main(String args[]) 
	{
		String recipients[] = null;
		String subj = null, mesg = null, from = null;
		if(args.length > 3)
		{
			from = args[0];
			subj = args[1];
			mesg = args[2];
			recipients = Arrays.copyOfRange(args, 3,args.length);
		}
		else
		{
			System.out.println("The Default syntax is <from> <subject> <mesg> <recipient 0> ...<recipient n>");
			return;
		}
			
		Socket_Mail test = new Socket_Mail();
		test.startnsend(from, subj, mesg, recipients);
	}




}