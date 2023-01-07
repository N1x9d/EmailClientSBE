package com.example.demo.models;

import jakarta.mail.Address;
import jakarta.mail.Message;

import java.util.Date;
import java.util.UUID;

public class Email {

  public static final Email DEFAULT = null;
  private String ID;
  private String Title;
  private String Message;
  private Address[] From;
  private Date ReceiveDate;

  private jakarta.mail.Message Self;
  public Email(Message mail) {
    Self = mail;
    ID = UUID.randomUUID().toString();
  }

  public String getTitle() {
    return Title;
  }

  public void setTitle(String title) {
    Title = title;
  }

  public String getMessage() {
    return Message;
  }

  public void setMessage(String message) {
    Message = message;
  }

  public Date getReceiveDate() {
    return ReceiveDate;
  }

  public void setReceiveDate(Date receiveDate) {
    ReceiveDate = receiveDate;
  }

  public Address[] getFrom() {
    return From;
  }

  public void setFrom(Address[] from) {
    From = from;
  }

  public String getID() {
    return ID;
  }

  public jakarta.mail.Message getSelf() {
    return Self;
  }
}
