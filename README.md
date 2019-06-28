# Gmail API test
This is a framework to test the Gmail API service with send and receive an email. It can help manual tester or anyone who need a regression test for the basic workflow of this popular mail service.

## Usage
### There are 2 ways to run this test
1. Run with Maven:
the user just needs to type ```mvn test``` and the test runner with start its job.

2. Run the test runner itself:
The runners are in this folder:

  > \src\test\java\_runners

Currently, there is just one basic test runner.

### Some notes for this framework
1. This framework required Java 8+ to run.
2. The test Gmail account credential is on this folder: \Gmail_API_test\tokens\StoredCredential

## Report
1. The framework will create the report base on the cucumber features that were run, the steps are automatically generated based on  cucumber scenarios
2. Each step of the report would have one or more images attached to it
3. When a test is failed, the report will show the error message

## Release Notes
### Release version v1.0
The basic user sending and verify email is implemented on this release. The framework will check the sender email, subject, content, and attachment if it is the same as it sent.

## What's next
1. Implement the getting email from receiver account feature.
2. Deep check the email content to avoid gliches.

## Demo
