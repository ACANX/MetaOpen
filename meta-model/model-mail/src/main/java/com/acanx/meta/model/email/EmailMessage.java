package com.acanx.meta.model.email;

import lombok.Data;

import java.util.List;

/**
 * EmailMessage
 *
 * @author ACANX
 * @since 20260507
 */
//@Builder
@Data
public class EmailMessage {

    private EmailSender sender;

    private List<EmailAddress> receivers;

//    private String name;

    private String subject;

    private List<String> tags;

    private String htmlContent;

    private List<String> attachments;

    public EmailMessage() {
    }
}
