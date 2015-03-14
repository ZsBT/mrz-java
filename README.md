# MRZ

Machine-Readable Zone (MRZ, see http://en.wikipedia.org/wiki/Machine-readable_passport ) parser for Java, as defined by ICAO: http://www.icao.int/


## Example

See the https://github.com/mvysny/mrz-java/blob/master/src/main/java/com/innovatrics/mrz/Demo.java file for example on usage. Simple usage:

```java
final MrzRecord record = MrzParser.parse("I<UTOSTEVENSON<<PETER<<<<<<<<<<<<<<<\nD231458907UTO3407127M9507122<<<<<<<2");
System.out.println("Name: " + record.givenNames + " " + record.surname);
```

## Usage with Maven 2/3

Add the baka.sk maven 2 repo to your maven installation - edit `~/.m2/settings.xml` so that it will look like the following:
```xml
<settings>
 <profiles>
  <profile>
   <id>default</id>
   <activation><activeByDefault>true</activeByDefault></activation>
   <repositories>
    <repository>
     <id>baka</id>
     <name>baka.sk</name>
     <url>http://www.baka.sk/maven2</url>
    </repository>
   </repositories>
  </profile>
 </profiles>
</settings>
```

Then add the following to your dependencies:

```xml
<dependency>
 <groupId>com.innovatrics.mrz</groupId>
 <artifactId>mrz-java</artifactId>
 <version>0.4</version>
</dependency>
```

## Changelog

### 0.4

- Fixed final checksum field, thanks to Marin Moulinier

### 0.3
- Added support for shortening names.

- Fixed name conversion: D'Artagnan gets properly converted to DARTAGNAN

- Added character expansion, e.g. Ä gets expanded to AE

### 0.2
- Added support for serializing record back to the MRZ form

- Fixed failing tests on Windows

- Added support for French ID Card records (Pierrick Martin)

- Added support for VISA A/B records (Jeremy Le Berre)

### 0.1
- Initial release

