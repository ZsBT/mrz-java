# MRZ

Machine-Readable Zone (MRZ, see http://en.wikipedia.org/wiki/Machine-readable_passport ) parser for Java, as defined by ICAO: http://www.icao.int/

## Branches

Branch "master" is for hotfixes only. For enhancements and minor bugfixes, please use branch "development".

## Example

See the https://github.com/ZsBT/mrz-java/blob/master/src/main/java/com/innovatrics/mrz/Demo.java file for example on usage. Simple usage:

```java
final MrzRecord record = MrzParser.parse("I<UTOSTEVENSON<<PETER<<<<<<<<<<<<<<<\nD231458907UTO3407127M9507122<<<<<<<2");
System.out.println("Name: " + record.givenNames + " " + record.surname);
```

## License
  Java parser for the MRZ records, as specified by the ICAO organization.
  Copyright (C) 2011 Innovatrics s.r.o.
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

Original maintainer (till 2017-05-28): Martin Vysny
