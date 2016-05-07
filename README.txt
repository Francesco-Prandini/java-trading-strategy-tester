Copyright (c) 2014, Francesco Prandini

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.




Di seguito riporto alcuni comandi necessari per utilizzare il software

1. Eseguire il Tester:

java -jar -Xms1000 -Xmx1000 Tesi.jar -tester



2. Eseguire il Datalogger:

java -jar Tesi.jar

i successivi argomenti sono:

- Quotazioni storiche:
	arg[0]="-hq"
	arg[1]--->file csv in input
	arg[2]---->databaseName
	arg[3]---->tableName
	arg[4]---->dateStart
	arg[5]---->dateEnd

- Quotazioni attuali:
	arg[0]--->file csv in input
	arg[1]--->databaseName
	arg[2]--->tableName
	arg[3]--->quoteProperties


3. Compilare una classe che implementa una strategia di trading:

javac -classpath Tesi.jar nomeClasse.java

il file .class ottenuto deve essere copiato nella cartella lib/tradingstrategies

