import java.util.Properties;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailMerge {

	public static void main(String[] args) {

		if(args.length<4) {
			System.err.println("Usage: java MailMerge username subject_file message_file list_file");
			System.exit(1);
		}
		final String username=args[0];
		String subjectFile = args[1];
		String messageFile = args[2];
		String listFile = args[3];

		final String password = new String(System.console().readPassword("%s", "Password:"));

		String subject=null;
		try {
			subject=new String(Files.readAllBytes(Paths.get(subjectFile)));
		} catch(Exception e) {
			System.err.println("Error in subject file");
			System.exit(2);
		}

		String message_text=null;
		try {
			message_text=new String(Files.readAllBytes(Paths.get(messageFile)));
		} catch(Exception e) {
			System.err.println("Error in message file");
			System.exit(2);
		}

		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(listFile));
		} catch(Exception e) {
			System.err.println("Error in list file");
			System.exit(3);
		}


		Properties props=new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Authenticator authenticator=new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};

		Session session=Session.getInstance(props, authenticator);

		String line;
		try {
			while((line=br.readLine())!=null) {

				String parts[]=line.split("\t");
				String recipient=parts[0];
				String message=constructMessage(message_text, parts);

				try {
					Message msg=new MimeMessage(session);
					msg.setFrom(new InternetAddress(username));
					msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
					msg.setSubject(subject);
					msg.setText(message);
					System.out.println("Sending mail to "+recipient);
					Transport.send(msg);
					System.out.println("Mail sent");
				} catch(AuthenticationFailedException e) {
					System.err.println("Username and Password are not accepted");
				} catch(MessagingException e) {
					System.err.println("Sending Failed.");
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			System.err.println("Error while sending mails");
			System.exit(4);
		}
	}

	public static String constructMessage(String message, String arr[]) {
		String res=message;
		for(int i=0;i<arr.length;i++) {
			res=res.replaceAll("\\{"+i+"\\}", arr[i]);
		}

		// replace all escaped ending braces
		res = res.replaceAll("\\\\\\}", "\\}");

		return res;
	}
}
