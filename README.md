<h1 align="center">
  :fire::coffee: firebase4j
</h1>

<p align="center">
   A plugin <b>Java</b> for <a href="https://firebase.google.com/">Firebase</a> applications
</p>

## :bulb: Installing

Add the dependency in your pom.xml

```xml
<dependency>
  <groupId>com.renz</groupId>
  <artifactId>firebase4j</artifactId>
  <version>1.0.0</version>
</dependency>
```

Execute the command

```command
mvn install
```

## :running: Usage

Connect to Firebase before using services

```java
try (FirebaseConnection firebaseConn = new FirebaseConnection()) {
			
  firebaseConn.connect("src/main/resources/YOUR_FIREBASE_ADMIN_SDK.json");

  // Code block using services
			
} catch (Exception e) {
  // exception handling
}
```

> To use Firestore, first you must configure your classes that will be persisted in the Database

Inherit the "Document" class in your collection model

```java
import com.renz.firebase4j.firestore.Document;

public class Person extends Document {

  private String name;
  private Integer age;

  // getter's and setter's
}
```

Create a repository that inherits from the "FirestoreRepository" class and give the constructor the type of document class you would like to handle

```java
public class PersonRepository extends FirestoreRepository<Person> {

  public PersonRepository() {
    super(Person.class);
  }
  
}
```

Now invoke the available methods


```java
try (FirebaseConnection firebaseConn = new FirebaseConnection()) {

  firebaseConn.connect("src/main/resources/YOUR_FIREBASE_ADMIN_SDK.json");

  PersonRepository personRep = new PersonRepository();

  Person person = personRep.save(person); // create document 

  List<Person> results = personRep.findAll(); // consulting collections

  personRep.delete(person.getId()); // search for a specific document by id 

} catch (Exception e) {
  // exception handling
}
```

Uploading files to Firebase Storage

```java
try (FirebaseConnection firebaseConn = new FirebaseConnection()) {

  firebaseConn.connect("src/main/resources/YOUR_FIREBASE_ADMIN_SDK.json");

  FirebaseStorage storage = new FirebaseStorage();

  storage.uploadFile(new File("YOUR_FILE.extension"));

} catch (Exception e) {
  // exception handling
}
```

## :computer: Want to help with the Project?

Create an "issue" and describe the features you would like in the application, or even bugs you found.

If you want to help with corrections, create your branch from `master` and open a Pull Request for me.

> :star: Could you favorite this repository? Just click on the star! Thank you very much!

## License 

MIT
