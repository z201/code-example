package cn.z201.email;

import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
public interface MailServiceI {

    void sendSimpleMail(String to, String subject, String content);

    void sendSimpleMail(String to, String subject, String content, File file);

    void sendSimpleMail(String to, String subject, Context context);

}
