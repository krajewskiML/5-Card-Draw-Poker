Przebieg gry:
1. Ante
2. Rozdanie kart
3. Każdy gracz może wymienić określoną ilość kart
4. Pierwsza tura licytacji
5. Każdy gracz może wymienić określoną ilość kart
6. Druga tura licytacji
7. Wyłonienie zwycięzcy/zwycięzców
8. Rozdzielenie puli na zwycięzców
9. Spytanie graczy czy chcą kontynuować rozgrywkę (jeżeli wszyscy gracze chcą przerwać grę to ją przerywamy)

Zasady gry którymi się się kierowałem podczas pisania można znaleźć w poniższym filmiku:
https://www.youtube.com/watch?v=bOyZbYjUcZg&t=159s

Jak uruchomić serwer?
Aby uruchomić serwer najpierw należy na głównym pom.xml uruchomić "mvn clean package". Następnie
należy uruchomić skompilowanego jara main2 w terminalu. Serwer spyta nas ilu graczy chcemy hostować. Następnie
serwer nasłuchuje na połączenia graczy. Po połączeniu zadeklarowanej liczby graczy serwer przeprowadza nas przez
rozgrywke.

Jak dołączyć do rozgrywki?
Aby dołączyć do rozgrywki trzeba również zbudować głownego pom.xml podobnie jak w instrukcji serwera. Następnie
należy uruchomić skompilowanego jara main1 w terminalu. Serwer poprosi nas o wpisanie nicku a potem
przeprowadzi nas przez rozgrywke.

Gdzie można znaleźć kod?
Link do githuba:

