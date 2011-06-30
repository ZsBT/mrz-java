@echo off
rem
rem Java parser for the MRZ records, as specified by the ICAO organization.
rem Copyright (C) 2011 Innovatrics s.r.o.
rem 
rem This library is free software; you can redistribute it and/or
rem modify it under the terms of the GNU Lesser General Public
rem License as published by the Free Software Foundation; either
rem version 2.1 of the License, or (at your option) any later version.
rem 
rem This library is distributed in the hope that it will be useful,
rem but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
rem Lesser General Public License for more details.
rem 
rem You should have received a copy of the GNU Lesser General Public
rem License along with this library; if not, write to the Free Software
rem Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
rem

if "%OS%"=="Windows_NT" @setlocal
SET CLSPATH=.
set JAVA_OPTS=-Xmx1024m
rem Add platform runtime libraries
for %%i in (lib\*.jar) do call :cpappend %%i
rem Launch the runtime
java %JAVA_OPTS% -classpath "%CLSPATH%" com.innovatrics.mrz.Demo %1 %2 %3 %4 %5 %6 %7 %8 %9
if "%OS%"=="Windows_NT" @endlocal
goto :EOF

:cpappend
	set CLSPATH=%CLSPATH%;%1
	goto :EOF

