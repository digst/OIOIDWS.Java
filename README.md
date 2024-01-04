When cloning the project to a Windows machine, make sure to add the flag

--config core.autocrlf=input

to ensure that linebreaks are preserved correctly in the shell scripts (Thanks Jonathan)

# Building locally

The integrations tests must be skipped because, they need various java and .net WSPs running.
So to build locally do:

`mvn clean install -DskipTests`

# Examples

## OIO-IDWS-SOAP

### Signature Case

#### Application
The swing application can be run from Idea. Ensure that the workspace is set to D:\Projects\OIOIDWS.Java\examples\oio-idws-soap\signature-scenario subfolder (talking your specific repo).

To make a successfull run you need to:
1. Start the service-bearer service in a prompt (or from the maven plugin in Idea if you want to debug the service)

`examples/oio-idws-soap/service-bearer> mvn clean install tomcat7:run-war`

2. Start the application by right-clicking examples/oio-idws-soap/signature-scenario/src/main/java/client/Application.java in Idea and selecting run Application.main. 
3. Click load `Open Keystore` and select `examples/oioidwsmoces.p12`. Enter password `Test1234`
4. Click `Run`
5. Observe the following output in the Application:
   ```
   Loaded keystore
   Invoked service
   Response from service: Hello John
   Response from service: Hello Jane
   ```

#### Unit test

The unit test resembles the application behavior. It also needs the service-bearer service to be running.

#### Troubleshooting

If the unit test and/or application does not work. One of the following things might be the reason:

**Update STS certificate** 

The latest STS certificate must be present in 
    `examples/oio-idws-soap/signature-scenario/src/main/resources/trust.jks`
    and in
    `examples/oio-idws-soap/service-bearer/src/main/resources/trust.jks`.
Use java keytool to check the alias `sts` in the keystores  

To delete the old do:

1. `cd <project with old sts certificate>`
2. `/c/tools/openjdk-8u232-b09/bin/keytool.exe -delete -alias sts -keystore src/main/resources/trust.jks -v`

To import the new do:

1. `cd <project with old sts certificate>`
2. `/c/tools/openjdk-8u232-b09/bin/keytool.exe -import -alias sts -file sts.cer -keystore src/main/resources/trust.jks`


The latest certificate can be downloaded from https://tu.nemlog-in.dk/oprettelse-og-administration-af-tjenester/security-token-service/hjaelp.og.vejledning/integrationstest/

**Ensure service certificate trust in client**

The certificate in `examples/oio-idws-soap/service-bearer/src/main/resources/service.pfx`
must be present in `examples/oio-idws-soap/signature-scenario/src/main/resources/trust.jks`

To export the certificate from service.pfx do (do keytool list to check if the alias has changed first)
1. `cd service-bearer`
2. `/c/tools/openjdk-8u232-b09/bin/keytool.exe -export -alias "eid java test (funktionscertifikat)" -file eid.cer -keystore src/main/resources/service.pfx`

To import the certificate into 
1. `cd signature-scenario`
2. `/c/tools/openjdk-8u232-b09/bin/keytool.exe -import -alias "eid java test (funktionscertifikat)" -file eid.cer  -keystore src/main/resources/trust.jks`

# Running all Integration Tests

`mvn clean install` in root folder

## Run signature system user case test (java WSC, java WSP):

### Start the service-hok Java WSP
`cd examples/oio-idws-soap/service-hok`
`mvn tomcat7:run-war`

### Run test
in another terminal:
goto folder

cd integrationtest\system-user-java
`mvn test`

stop the service-hok from before

## Run signature system user case test (java WSC, .Net WSP):

### Start .Net WSP
In the OIOIDWS.dotnet project start the Digst.OioIdws.WspExample

goto folder
`cd integrationtest\system-user-dotnet`
`mvn test`

## Run signature bearer case test (Java WSC, Java WSP):

### Start the service-bearer WSP
`cd examples/oio-idws-soap/service-bearer`
`mvn tomcat7:run-war`

### Run test
in another terminal
`cd integrationtest\signature-scenario-java`
`mvn test`

stop the service-bearer from above

## Run REST signaure case (Java WSC, Java WSP and Java WSC, .Net WSP)

### Start the Java REST WSP
`cd examples/oio-idws-rest/rest-service`
`mvn spring-boot:run`

### Start the .Net REST WSP
In the OIOIDWS.dotnet project start the Digst.OioIdws.Rest.Examples.ServerAndASCombined

### Run test
in another terminal
`cd integrationtest\rest-signature`
`mvn test -Dtest=signature.RestSignatureScenarioJavaTest`
`mvn test -Dtest=signature.RestSignatureScenarioDotnetTest`

(you need to run the tests separately since the WSC uses the same port in both tests)

stop the Java and .Net REST WSPs

## Run SOAP bootstrap case (Java WSC, Java WSP and Java WSC, .Net WSP)

NB! Currently you need to enter the password for the MOCES certificate, since it runs against int test with no Test Login.
Because of this it is currently ignored, so you need to comment out the `@Ignore` statement in the `TestBootstrapScenario_IntTest()` 
method of the integrationtest/bootstrap/src/test/java/soap/BootstrapIntegrationTest.java class.

### Start the service-hok Java WSP
`cd examples/oio-idws-soap/service-hok`
`mvn tomcat7:run-war`

### Start .Net Wsp
In the OIOIDWS.dotnet project start the Digst.OioIdws.WspExample

### Start the Bootstrap web
`cd examples/oio-idws-soap/bootstrap-scenario`
`mvn tomcat7:run-war`

### Run test
in another terminal
`cd integrationtest\boostrap`
`mvn test`

NB! The tests are not fully automated yet, so you need to enter the password manually in the GUI. 

stop the Java and .Net WSPs

## Run REST bootstrap case (Java WSC, Java WSP and Java WSC, .Net WSP)

NB! Currently you need to enter the password for the MOCES certificate, since it runs against int test with no Test Login.
Because of this it is currently ignored, so you need to comment out the `@Ignore` statement in the `RestBootstrapScenarioDotnetTest` and `RestBootstrapScenarioJavaTest` 
method of the integrationtest/bootstrap/src/test/java/soap/BootstrapIntegrationTest.java class.

### Start the Java REST WSP
`cd examples/oio-idws-rest/rest-service`
`mvn spring-boot:run`

### Start the .Net REST WSP
In the OIOIDWS.dotnet project start the Digst.OioIdws.Rest.Examples.ServerAndASCombined

### Run test
in another terminal
`cd integrationtest\rest-bootstrap`
`mvn test -Dtest=bootstrap.RestBootstrapScenarioJavaTest`
`mvn test -Dtest=bootstrap.RestBootstrapScenarioDotnetTest`

(you need to run the tests separately since the WSC uses the same port in both tests)

stop the Java and .Net REST WSPs

