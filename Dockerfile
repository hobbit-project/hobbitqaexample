FROM java
ADD target/hobbitqaexample-0.0.1-SNAPSHOT.jar /hobbitqaexample/hobbitqaexample-0.0.1-SNAPSHOT.jar
WORKDIR /hobbitqaexample
CMD java -cp MyShadedJar.jar org.hobbit.core.run.ComponentStarter org.hobbit.examplesystem.hobbitqaexample.ExampleQaldSystemAdapter