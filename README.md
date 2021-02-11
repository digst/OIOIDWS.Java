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
3. Click load `Open Keystore` and select `examples/test-moces.pfx`. Enter password `Test1234`
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

The latest certificate can be downloaded from https://test-nemlog-in.dk/Testportal/certifikater/IntegrationTestSigning.zip 

**Ensure service certificate trust in client**

The certificate in `examples/oio-idws-soap/service-bearer/src/main/resources/service.pfx`
must be present in `examples/oio-idws-soap/signature-scenario/src/main/resources/trust.jks`

To export the certificate from service.pfx do (do keytool list to check if the alias has changed first)
1. `cd service-bearer`
2. `/c/tools/openjdk-8u232-b09/bin/keytool.exe -export -alias "eid java test (funktionscertifikat)" -file eid.cer -keystore src/main/resources/service.pfx`

To import the certificate into 
1. `cd signature-scenario`
2. `/c/tools/openjdk-8u232-b09/bin/keytool.exe -import -alias "eid java test (funktionscertifikat)" -file eid.cer  -keystore src/main/resources/trust.jks`
