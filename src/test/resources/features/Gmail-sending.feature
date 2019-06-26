@API @Gmail
Feature: Sending mail via Gmail API

  @SendEmail @ReceiveEmail
  Scenario Outline: Sending mail via Gmail API
    Given <Sender_email> is sending an email to <Receiver_email> from <Sender_name> to <Receiver_name> with <Title> title
    And <Receiver_email> retrieve the email
    Then <Receiver_email> received the right email

    Examples:
      | Sender_email              | Receiver_email            | Sender_name | Receiver_name | Title       |
      | jason.pham.qa.1@gmail.com | jason.pham.qa.2@gmail.com | Hung        | Nick          | My new bike |