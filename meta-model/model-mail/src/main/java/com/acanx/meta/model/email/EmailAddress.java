package com.acanx.meta.model.email;

import lombok.Data;

/**
 * EmailAddress
 *
 * @author ACANX
 * @since 20260507
 */
@Data
public class EmailAddress {

    private String name;

    private String address;


    public EmailAddress(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
