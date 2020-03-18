package com.roy.account.model;

import lombok.Data;

import java.util.List;

/**
 * @author chenlin
 */
@Data
public class Contact {

    private Integer uid;

    private List<Integer> contactUidList;
}
