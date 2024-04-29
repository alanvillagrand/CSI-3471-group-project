package org.bearluxury.Email;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
//
public class EmailSender {
    public static void sendConfirmationEmail(String recipient,String emailAddress ){
        Email email = EmailBuilder.startingBlank()
                .from("Baylor Bear Luxury", "nicholas_nolen1@baylor.edu")
                .to(recipient, emailAddress)
                .withSubject("Hey ;)")
                .withPlainText("Thank you for Registering!")
                .buildEmail();

        MailerBuilder
                .withSMTPServer("mail.smtp2go.com", 2525, "nicholas_nolen1@baylor.edu", "xltlHLbGWIM6Q6iB")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
//                .withProxy("socksproxy.host.com", 1080, "proxy user", "proxy password")
//                .withSessiosnTimeout(10 * 1000)
//                .clearEmailValidator() // turns off email validation
//                .withEmailValidator( // or not
//                        JMail.strictValidator()
//                                .requireOnlyTopLevelDomains(TopLevelDomain.DOT_COM)
//                                .withRule(email -> email.localPart().startsWith("allowed")))
//                .withProperty("mail.smtp.sendpartial", true)
//                .withDebugLogging(true)
//                .async()
                .buildMailer()
                .sendMail(email);
    }
}
