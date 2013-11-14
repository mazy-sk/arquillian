This is a test project for demonstration JEE technology.

## Tests in the project

### GreeterTest
Test CDI - Greeter inject PhraseBuilder

### BasketTest
Uses CDI and EJB working together

### MessageClientTest
Test JMS service - Session bean as a client send text message to message driven bean
Run test: `mvn test -Parquillian-glassfish-embedded -Dtest=MessageClientTest`


