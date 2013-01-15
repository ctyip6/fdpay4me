package com.bbone.appengine;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Upload extends HttpServlet {


    private static final Logger LOGGER = Logger.getLogger(Upload.class.getName
            ());
    private static final String IMG_PATH = "http://payitexotest.appspot" +
            ".com/serve?blob-key=";
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    private String composeEmail(Entity order) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>name: ").append(order.getProperty("name")).append
                ("</p>");
        sb.append("<p>email: ").append(order.getProperty("email")).append
                ("</p>");
        sb.append("<p>mobile: ").append(order.getProperty("mobile")).append
                ("</p>");
        sb.append("<p>address: ").append(order.getProperty("address")).append
                ("</p>");
        sb.append("<p><img src='").append(IMG_PATH).append(order.getProperty
                ("blobKey")).append("' /></p>");
        return sb.toString();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        BlobKey blobKey = blobs.get("myFile").get(0);

        // store the form
        Entity order = new Entity("Order");
        order.setProperty("name", req.getParameter("name"));
        order.setProperty("email", req.getParameter("email"));
        order.setProperty("mobile", req.getParameter("mobile"));
        order.setProperty("address", req.getParameter("address"));
        order.setProperty("blobKey", blobKey.getKeyString());

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(order);

        res.sendRedirect("/");

        // email to shop keeper
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            Message message = new MimeMessage(session);


            message.setFrom(new InternetAddress("ctyip6@gmail.com",
                    "Jimmy Yip"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("ctyip6@gmail.com", "Jimmy Yip"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("royroycat@gmail.com", "Roy Chung"));
            message.setSubject("New Order");

            String htmlBody = composeEmail(order);
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            mp.addBodyPart(htmlPart);
            message.setContent(mp);
            Transport.send(message);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot send email", e);
        }
    }
}